package com.outbox.sample.kafka.core.event

import org.apache.avro.specific.SpecificRecord

interface EventRepository {
    fun createEvent(messageTopic: String, messageKey: String, messagePayload: ByteArray): EventEntity
    fun <T : SpecificRecord> createEvent(messageTopic: String, messageKey: String, message: T): EventEntity
}