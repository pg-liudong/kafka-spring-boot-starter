package org.dong.kafka.register;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.text.StrFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.dong.kafka.model.KafkaConsumer;
import org.dong.kafka.model.KafkaProducer;
import org.dong.kafka.model.KafkaPropertiesWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Kafka数据源注册
 *
 * @author liudong
 * @date 2022/1/27 16:09
 */
@Slf4j
@Component
public class KafkaDataSourceRegister implements InitializingBean {

    @Resource
    private KafkaPropertiesWrapper kafkaPropertiesWrapper;

    @Resource
    private DefaultListableBeanFactory beanFactory;

    /**
     * 生产者配置
     *
     * @return 配置信息
     */
    private Map<String, Object> producerConfig(KafkaProducer producer) {
        Map<String, Object> producerConfigs = new HashMap<>(16);
        producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producer.getBootstrapServers());
        producerConfigs.put(ProducerConfig.LINGER_MS_CONFIG, producer.getLingerMs());
        producerConfigs.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, producer.getMaxRequestSize());
        producerConfigs.put(ProducerConfig.BATCH_SIZE_CONFIG, producer.getBatchSize());
        producerConfigs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producer.getBufferMemory());
        producerConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return producerConfigs;
    }


    /**
     * 消费者配置
     *
     * @return 配置信息
     */
    private Map<String, Object> consumerConfig(KafkaConsumer consumer) {
        Map<String, Object> consumerConfigs = new HashMap<>(16);
        consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumer.getBootstrapServers());
        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, consumer.getGroupId());
        consumerConfigs.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, consumer.getHeartbeatInterval());
        consumerConfigs.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, consumer.getFetchMinSize());
        consumerConfigs.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, consumer.getFetchMaxWait());
        consumerConfigs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumer.getMaxPollRecords());
        consumerConfigs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumer.getEnableAutoCommit());
        consumerConfigs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumer.getSessionTimeoutMs());
        consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumer.getAutoOffsetReset());
        consumerConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return consumerConfigs;
    }


    @Override
    public void afterPropertiesSet() {
        if (ObjectUtils.isEmpty(kafkaPropertiesWrapper) || ObjectUtils.isEmpty(kafkaPropertiesWrapper.getDatasource())) {
            return;
        }
        Assert.isTrue(StringUtils.isNotBlank(kafkaPropertiesWrapper.getPrimary()), "kafka not setting primary datasource.");
        kafkaPropertiesWrapper.getDatasource().forEach((datasourceName, datasource) -> {
            datasource = getDataSource(datasource);
            if (ObjectUtils.isEmpty(datasource)) {
                return;
            }
            if (ObjectUtils.isNotEmpty(datasource.getProducer())) {
                Assert.isTrue(StringUtils.isNotBlank(datasource.getProducer().getKafkaTemplate()), "kafka-template is null not allowed.");
                DefaultKafkaProducerFactory<Object, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerConfig(datasource.getProducer()));
                KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);
                beanFactory.registerSingleton(datasource.getProducer().getKafkaTemplate(), kafkaTemplate);
                log.info("kafka-multiple-datasource => add a kafka template named [{}] success.", datasource.getProducer().getKafkaTemplate());
            }
            if (ObjectUtils.isNotEmpty(datasource.getConsumer())) {
                Assert.isTrue(StringUtils.isNotBlank(datasource.getConsumer().getContainerFactory()), "concurrent-kafka-listener-container-factory is null not allowed.");
                DefaultKafkaConsumerFactory<Object, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfig(datasource.getConsumer()));
                ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(consumerFactory);
                factory.getContainerProperties().setPollTimeout(1500);
                if (!datasource.getConsumer().getEnableAutoCommit()) {
                    factory.getContainerProperties().setAckMode((ContainerProperties.AckMode.MANUAL));
                }
                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(DefaultKafkaConsumerFactory.class)
                        .setScope(BeanDefinition.SCOPE_SINGLETON)
                        .getBeanDefinition();
                if (Objects.equals(kafkaPropertiesWrapper.getPrimary(), datasourceName)) {
                    beanDefinition.setPrimary(true);
                }
                String consumerFactoryBeanName = StrFormatter.format("{}{}", datasourceName, "ConsumerFactory");
                beanFactory.registerBeanDefinition(consumerFactoryBeanName, beanDefinition);
                beanFactory.registerSingleton(consumerFactoryBeanName, consumerFactory);
                beanFactory.registerSingleton(datasource.getConsumer().getContainerFactory(), factory);
                log.info("kafka-multiple-datasource => add a kafka listener container factory named [{}] success.", datasource.getConsumer().getContainerFactory());
            }
        });
        log.info("kafka-multiple-datasource initial loaded [{}] datasource, primary datasource named [{}]", kafkaPropertiesWrapper.getDatasource().size(), kafkaPropertiesWrapper.getPrimary());
    }

    /**
     * 获取数据源
     *
     * @param datasource 数据源参数
     * @return @{@link KafkaPropertiesWrapper}
     */
    private KafkaPropertiesWrapper getDataSource(KafkaPropertiesWrapper datasource) {
        KafkaPropertiesWrapper propertiesWrapper = new KafkaPropertiesWrapper();
        BeanUtil.copyProperties(kafkaPropertiesWrapper, propertiesWrapper);
        if (ObjectUtils.isEmpty(datasource)) {
            return propertiesWrapper;
        }
        BeanUtil.copyProperties(datasource.getProducer(), propertiesWrapper.getProducer(), CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        BeanUtil.copyProperties(datasource.getConsumer(), propertiesWrapper.getConsumer(), CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        return propertiesWrapper;
    }

}
