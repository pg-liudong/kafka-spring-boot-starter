package org.dong.kafka.model;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * kafka生产者
 *
 * @author liudong
 * @date 2022/1/26 13:26
 */
@Data
public class KafkaProducer {

    /**
     * kafka template
     */
    private String kafkaTemplate;

    /**
     * 格式为host[:port]例如localhost:9092,是kafka连接的broker地址列表,可以是多台,用逗号分隔
     */
    private List<String> bootstrapServers = Lists.newArrayList("localhost:9092");

    /**
     * 代表kafka收到消息的答复数,0就是不要答复,爱收到没收到.1就是有一个leader broker答复就行,all是所有broker都要收到才行
     * 0: Producer不等待kafka服务器的答复,消息立刻发往socket buffer,这种方式不能保证kafka收到消息,设置成这个值的时候retries参数就失效了,因为producer不知道kafka收没收到消息,所以所谓的重试就没有意义了,发送返回值的offset全默认是-1.
     * 1: 等待leader记录数据到broker本地log即可.不等待leader同步到其他followers,那么假如此时刚好leader收到消息并答复后,leader突然挂了,其他fowller还没来得及复制消息呢,那么这条消息就会丢失了.
     * all:等待所有broker记录消息.保证消息不会丢失(只要从节点没全挂),这种方式是最高可用的 acks默认值是1.
     */
    private Integer acks = 1;

    /**
     * 批处理消息字节数,发往broker的消息会包含多个batches,每个分区对应一个batch,batch小了会减小响吞吐量,batch为0的话就禁用了batch发送,默认值:16384(16kb)
     */
    private Integer batchSize = 16384;

    /**
     * 当消息发送速度大于kafka服务器接收的速度,producer会阻塞max_block_ms,超时会报异常,buffer_memory用来保存等待发送的消息,默认33554432(32MB)
     */
    private Long bufferMemory = 33554432L;

    /**
     * 客户端名称,用来追查日志的,默认是kafka-producer-# (#是个唯一编号)
     */
    private String clientId = "kafka-producer";

    /**
     * 发消息时候的压缩类型可以是gzip,snappy,lz4,None,压缩是针对batches的,所以batches的大小会影响压缩效率,大一点的压缩比例可能好些,要是太小的话压缩就没有意义了,比如你就发个几个字节的数据那压完没准更大了.至于什么时候启用压缩,要看应用场景,启用后producer会变慢,但网络传输带宽占用会减少,带宽紧缺建议开启压缩,带宽充足就不用开了 默认值:None
     */
    private String compressionType = "None";

    /**
     * key序列化函数. 默认值: None.
     */
    private Class<?> keySerializer = StringSerializer.class;

    /**
     * 值序列化函数默认值: None.
     */
    private Class<?> valueSerializer = StringSerializer.class;

    /**
     * 重试发送次数,有时候网络出现短暂的问题的时候,会自动重发消息,前面提到了这个值是需要在acks=1或all时候才有效.如果设置了该参数,但是setting max_in_flight_requests_per_connection没有设置为1的话,可能造成消息顺序的改变,因为如果2个batches发到同一个partition,但是第一个失败重发了,那么就会造成第二个batches跑到前面去了. Default: 0.
     */
    private Integer retries = 0;

    /**
     * 生产者每次发送消息的时间间隔（毫秒）
     */
    private Integer lingerMs = 0;

    /**
     * 单条消息最大值（字节）
     */
    private Integer maxRequestSize = 52428800;

    /**
     * 当非空时，为生产者启用事务支持。
     */
    private String transactionIdPrefix;

    /**
     * 用于配置客户端的其他特定于生产者的属性。
     */
    private final Map<String, String> properties = new HashMap<>();
    
}
