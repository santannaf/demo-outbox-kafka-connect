package com.outbox.sample.kafka.core.event

import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.specific.SpecificRecord
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
@Transactional
class JPAEventRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
    private val kafkaAvroSerializer: KafkaAvroSerializer
) : EventRepository {
    override fun createEvent(messageTopic: String, messageKey: String, messagePayload: ByteArray): EventEntity {
        val entity = EventEntity(
            messageTopic = messageTopic,
            messageKey = messageKey,
            messagePayload = Base64.getEncoder().encodeToString(messagePayload)
        )
        entityManager.persist(entity)
        return entity
    }

    override fun <T : SpecificRecord> createEvent(messageTopic: String, messageKey: String, message: T): EventEntity {
        val messageBytes: ByteArray = kafkaAvroSerializer.serialize(messageTopic, message)
        return createEvent(messageTopic, messageKey, messageBytes)
    }
}