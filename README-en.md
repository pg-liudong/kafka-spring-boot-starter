<p align="right">
  <a href="https://github.com/pg-liudong/kafka-spring-boot-starter/blob/main/README.md">
   <img alt="中文" src="https://img.shields.io/badge/-中文-blue">
  </a>
</p>

<p align="center">
  <a href="https://github.com/pg-liudong/kafka-spring-boot-starter">
   <img alt="Mybatis-Plus-Logo" src="https://raw.githubusercontent.com/pg-liudong/pic-bed/main/202202131718547.svg">
  </a>
</p>

<p align="center">
  Encapsulation based on spring-kafka not only supports native configuration, but also adds multi data source configuration.
</p>

<p align="center">
  <a href="https://mvnrepository.com/artifact/io.github.pg-liudong/kafka-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/maven-central/v/io.github.pg-liudong/kafka-spring-boot-starter.svg?style=flat-square">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>

# Intro

Kafka-spring-boot-starter is encapsulated based on spring-kafka. In addition to supporting native configuration, it also adds multi data source configuration.

QQ：908942659 ； wechat：xin2014555

# Install

``` xml
<dependency>
  <groupId>io.github.pg-liudong</groupId>
  <artifactId>kafka-spring-boot-starter</artifactId>
  <version>1.0.5</version>
</dependency>
```
  
# Features


- **Support native：** Support spring-kafka native configuration.

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

- **Support multiple data sources：** Support multi data source configuration.(The smaller the configuration granularity, the higher the priority)

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

# Demo

- [kafka-spring-boot-starter-demo](https://github.com/pg-liudong/kafka-spring-boot-starter-demo)

# Donate

If you think the plug-in is great and saves you a lot of time, invite the author to have a cup of coffee~ ☕☕☕, Thank you. Your support is the driving force to encourage us to move forward. No matter how much it is, it is enough to express your intention.
| ![微信](https://raw.githubusercontent.com/pg-liudong/pic-bed/main/wechat.jpg) | ![支付宝](https://raw.githubusercontent.com/pg-liudong/pic-bed/main/AliPay.jpg) |[![Paypal](https://raw.githubusercontent.com/pg-liudong/pic-bed/main/202202101456821.png)](https://paypal.me/3228389063) |
| --- | --- | --- |

# WeChat official account

Irregularly share the dry notes of programming technology, not limited to algorithms, databases, spring boot, microservices, high concurrency, JVM, docker, elk, programming skills and other related knowledge. We look forward to making progress with you!

| ![微信公众号](https://raw.githubusercontent.com/pg-liudong/pic-bed/main/微信公众号.png) |
| --- |

