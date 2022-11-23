package com.outbox.sample.kafka.core.user

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import web.core.user.pub.UserChangedMessage

@Service
class OnUserChanged {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["user.changed.kconnect"], groupId = "sampleApp.onUserChanged")
    fun onUserChanged(message: UserChangedMessage, ack: Acknowledgment) {
        log.info("user created, id={}, name={}", message.getUserId(), message.getUserName())
        ack.acknowledge()
    }
}