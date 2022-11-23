package com.outbox.sample.kafka.core.user

data class UserCreateForm(
    val email: String,
    val name: String,
    val age: Int,
    val addressPostalCode: String
)