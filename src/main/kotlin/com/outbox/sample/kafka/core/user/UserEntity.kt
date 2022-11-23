package com.outbox.sample.kafka.core.user

import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Type
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "User")
@Table(
    name = "user",
    indexes = [
        Index(name = "user_uk01", columnList = "email", unique = true)
    ]
)
@DynamicUpdate
data class UserEntity(
    @Id
    @Type(type = "uuid-binary")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GenericGenerator(name = "UserId", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID = UUID.randomUUID(),
    @Column(updatable = false, nullable = false)
    val email: String,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val age: Int,
    val addressPostalCode: String,
    @Column(nullable = false, updatable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    @Column(nullable = false)
    val updatedAt: ZonedDateTime = createdAt
)
