package com.outbox.sample.kafka.config

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties.AckMode
import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.BackOffPolicy
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.AlwaysRetryPolicy
import org.springframework.retry.support.RetryTemplate
import java.util.concurrent.TimeUnit

@EnableKafka
class KafkaConfig(
    @Value("\${kafka.bootstrapAddress}")
    private val kafkaBootstrapAddress: String,
    @Value("\${kafka.schemaRegistryUrl}")
    private val schemaRegistryURL: String,
    @Value("\${kafka.consumer.groupId}")
    private val kafkaConsumerGroupId: String
) {
    @Bean
    fun kafkaAvroSerializer(): KafkaAvroSerializer {
        val kafkaAvroSerializer = KafkaAvroSerializer()
        val serCfg: MutableMap<String, String?> = HashMap()
        serCfg[KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistryURL
        kafkaAvroSerializer.configure(serCfg, false)
        return kafkaAvroSerializer
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaBootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java
        props[KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistryURL
        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Any>): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory)
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaBootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = kafkaConsumerGroupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaAvroDeserializer::class.java
        props[KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG] = "true"
        props[KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistryURL
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, Any>): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = consumerFactory
        factory.setConcurrency(6)
        factory.setRetryTemplate(retryTemplate())
        factory.containerProperties.ackMode = AckMode.MANUAL
        return factory
    }

    private fun retryTemplate(): RetryTemplate {
        val template = RetryTemplate()
        template.setRetryPolicy(retryPolicy())
        template.setBackOffPolicy(backOffPolicy())
        return template
    }

    private fun retryPolicy(): RetryPolicy {
        return AlwaysRetryPolicy()
    }

    private fun backOffPolicy(): BackOffPolicy {
        val policy = ExponentialBackOffPolicy()
        policy.maxInterval = TimeUnit.SECONDS.toMillis(30)
        return policy
    }
}