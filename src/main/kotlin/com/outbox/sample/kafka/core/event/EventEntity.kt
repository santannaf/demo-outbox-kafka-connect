package com.outbox.sample.kafka.core.event

import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "Event")
@Table(
    name = "event",
    indexes = [
        Index(name = "event_ix01", columnList = "createdAt")
    ]
)
@DynamicUpdate
data class EventEntity(
    @Id
    @Type(type = "uuid-binary")
    @Column(columnDefinition = "binary(16)")
    private val id: UUID = UUID.randomUUID(),
    @Column(nullable = false, updatable = false)
    private val createdAt: ZonedDateTime = ZonedDateTime.now(),
    @Column(nullable = false, updatable = false)
    private var messagePayload: String,
    @Column(nullable = false, updatable = false)
    private var messageKey: String,
    @Column(nullable = false, updatable = false)
    private var messageTopic: String
)
