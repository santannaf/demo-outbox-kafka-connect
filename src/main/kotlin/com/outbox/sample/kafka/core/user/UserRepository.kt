package com.outbox.sample.kafka.core.user

interface UserRepository {
    fun findUserByEmail(email: String): UserEntity?
}