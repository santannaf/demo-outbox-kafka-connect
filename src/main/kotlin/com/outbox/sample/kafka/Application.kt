package com.outbox.sample.kafka

import com.outbox.sample.kafka.config.KafkaConfig
import com.outbox.sample.kafka.config.WebConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
@Import(WebConfig::class, KafkaConfig::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(args = args)
}
