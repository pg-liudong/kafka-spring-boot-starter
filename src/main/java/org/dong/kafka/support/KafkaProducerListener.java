package org.dong.kafka.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * kafka生产者监听器
 *
 * @author liudong
 * @date 2021/10/14
 */
@Slf4j
@Component
public class KafkaProducerListener<K, V> implements ProducerListener<K, V> {

    /**
     * kafka发送成功回调
     *
     * @param producerRecord 记录
     * @param recordMetadata 源数据
     */
    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("key:[{}], topic:[{}], message:[{}], partition:[{}], result: [Send message to kafka success.]", producerRecord.key(), producerRecord.topic(), producerRecord.value(), producerRecord.partition());
    }

    /**
     * kafka发送失败回调
     *
     * @param producerRecord 记录
     * @param recordMetadata 源数据
     * @param exception      异常
     */
    @Override
    public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("key:[{}], topic:[{}], message:[{}], partition:[{}], result: [Send message to kafka failed.]", producerRecord.key(), producerRecord.topic(), producerRecord.value(), producerRecord.partition(), exception);
    }

}
