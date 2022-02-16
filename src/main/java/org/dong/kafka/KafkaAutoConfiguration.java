package org.dong.kafka;

import org.dong.kafka.model.KafkaPropertiesWrapper;
import org.dong.kafka.register.KafkaDataSourceRegister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * kafka自动装配
 *
 * @author liudong
 * @date 2022/1/26 15:09
 */
@EnableKafka
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@EnableConfigurationProperties(KafkaPropertiesWrapper.class)
@Import({KafkaDataSourceRegister.class})
public class KafkaAutoConfiguration implements BeanFactoryAware, SmartInstantiationAwareBeanPostProcessor {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        beanFactory.getBean(KafkaDataSourceRegister.class);
    }

}
