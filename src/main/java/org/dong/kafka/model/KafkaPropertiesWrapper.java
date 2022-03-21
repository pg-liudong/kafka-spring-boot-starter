package org.dong.kafka.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * kafka配置包装类
 *
 * @author liudong
 * @date 2022/1/26 16:39
 */
@Data
@Configuration
@ConfigurationProperties("spring.kafka.multiple")
public class KafkaPropertiesWrapper {

    /**
     * 主数据源
     */
    private String primary;

    /**
     * Kafka消费服务器
     */
    private List<String> bootstrapServers;

    /**
     * 消费者
     */
    @NestedConfigurationProperty
    private KafkaConsumer consumer;

    /**
     * 生产者
     */
    @NestedConfigurationProperty
    private KafkaProducer producer;

    /**
     * 数据源
     */
    private Map<String, KafkaPropertiesWrapper> datasource;

}
