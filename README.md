<p align="right">
  <a href="https://github.com/pg-liudong/kafka-spring-boot-starter/blob/main/README.md">
   <img alt="English" src="https://img.shields.io/badge/-English-blue">
  </a>
</p>

<p align="center">
  <a href="https://github.com/pg-liudong/kafka-spring-boot-starter">
   <img alt="Mybatis-Plus-Logo" src="https://raw.githubusercontent.com/pg-liudong/pic-bed/main/202202131718547.svg">
  </a>
</p>

<p align="center">
  基于spring-kafka进行封装，支持原生配置，支持多数据源配置。
</p>

<p align="center">
  <a href="">
    <img alt="maven" src="https://img.shields.io/maven-central/v/io.github.pg-liudong/kafka-spring-boot-starter.svg?style=flat-square">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>

# 简介 | Intro

Kafka-spring-boot-starter 除了支持原生 spring-kafka 配置外，还增加了多数据源配置。

QQ：908942659 ； wechat：xin2014555

# 安装 | Install

``` xml
<dependency>
  <groupId>io.github.pg-liudong</groupId>
  <artifactId>kafka-spring-boot-starter</artifactId>
  <version>1.0.5</version>
</dependency>
```
  
# 特征 | Features


- **支持原生**：支持spring-kafka原生配置。

``` xml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      auto-offset-reset: earliest
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```

- **支持多数据源**：支持多数据源配置。（配置粒度越小，优先级越高，遵循就近原则）

``` xml
spring:
  kafka:
    multiple:
      # Primary data source is not allowed to be empty.
      primary: ds1  
      consumer:
        key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
        value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
        auto-offset-reset: latest
      producer:
        key-serializer: org.apache.kafka.common.serialization.StringSerializer
        value-serializer: org.apache.kafka.common.serialization.StringSerializer
      datasource:
        ds1:
          bootstrap-servers: localhost:9092
          consumer:
            # Consumer concurrent kafka listener container factory is not allowed to be empty.
            container-factory: ds1KafkaListenerContainerFactory  
            auto-offset-reset: earliest
          producer:
            kafka-template: ds1KafkaTemplate
        ds2:
          bootstrap-servers: localhost:9093
          consumer:
            container-factory: ds2KafkaListenerContainerFactory
            auto-offset-reset: earliest
          producer:
            # Producer kafka template bean name is not allowed to be empty.
            kafka-template: ds2KafkaTemplate 
```

# 演示 | Demo

- [kafka-spring-boot-starter-demo](https://github.com/pg-liudong/kafka-spring-boot-starter-demo)


# 捐赠 | Donate

如果觉得插件很赞，为你节约了不少时间，那么就请作者喝杯咖啡吧~☕☕☕，非常感谢！
| ![微信](https://raw.githubusercontent.com/pg-liudong/pic-bed/main/wechat.jpg) | ![支付宝](https://raw.githubusercontent.com/pg-liudong/pic-bed/main/AliPay.jpg) |[![Paypal](https://raw.githubusercontent.com/pg-liudong/pic-bed/main/202202101456821.png)](https://paypal.me/3228389063) |
| --- | --- | --- |

# 微信公众号 | WeChat official account

不定期分享编程技术干货笔记，不限于算法、数据库、Spring Boot、微服务、高并发、JVM、Docker、ELK、编程技巧等相关知识，期待与您共同进步！

| ![微信公众号](https://raw.githubusercontent.com/pg-liudong/pic-bed/main/微信公众号.png) |
| --- |

