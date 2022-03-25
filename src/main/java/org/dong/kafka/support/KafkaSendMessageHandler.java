package org.dong.kafka.support;

import cn.hutool.core.lang.Assert;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 发送kafka消息处理器
 *
 * @author liudong
 * @date 2022/3/25 9:29
 */
@Component
public class KafkaSendMessageHandler<K, V> {

    /**
     * 同步发送消息
     *
     * @param kafkaTemplate    kafka template
     * @param topic            topic
     * @param message          message
     * @param producerListener producer listener
     * @return 发送结果
     */
    public Boolean syncSendKafka(KafkaTemplate<K, V> kafkaTemplate, String topic, V message, ProducerListener<K, V> producerListener) {
        Assert.notNull(ObjectUtils.isNotEmpty(topic), "Send message to kafka topic cannot be empty.");
        Assert.notNull(ObjectUtils.isNotEmpty(message), "Send message to kafka data cannot be empty.");
        try {
            if (ObjectUtils.isNotEmpty(producerListener)) {
                kafkaTemplate.setProducerListener(producerListener);
            }
            kafkaTemplate.send(topic, message).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Send message to Kafka failed.", e);
        }
        return true;
    }

    /**
     * 同步发送消息
     *
     * @param kafkaTemplate kafka template
     * @param topic         topic
     * @param message       消息
     * @return 发送结果
     */
    public Boolean syncSendKafka(KafkaTemplate<K, V> kafkaTemplate, String topic, V message) {
        return syncSendKafka(kafkaTemplate, topic, message, null);
    }

    /**
     * 异步发送数据
     *
     * @param kafkaTemplate    kafka template
     * @param topic            topic
     * @param message          消息
     * @param producerListener producer listener
     */
    public void asyncSendKafka(KafkaTemplate<K, V> kafkaTemplate, String topic, V message, ProducerListener<K, V> producerListener) {
        Assert.notNull(ObjectUtils.isNotEmpty(topic), "Send message to kafka topic cannot be empty.");
        Assert.notNull(ObjectUtils.isNotEmpty(message), "Send message to kafka data cannot be empty.");
        if (ObjectUtils.isNotEmpty(producerListener)) {
            kafkaTemplate.setProducerListener(producerListener);
        }
        kafkaTemplate.send(topic, message);
    }

    /**
     * 异步发送数据
     *
     * @param kafkaTemplate kafka template
     * @param topic         topic
     * @param message       消息
     */
    public void asyncSendKafka(KafkaTemplate<K, V> kafkaTemplate, String topic, V message) {
        asyncSendKafka(kafkaTemplate, topic, message, null);
    }


}
