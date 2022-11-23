package com.outbox.sample.kafka.core.exceptions

class UnknownPostalCodeException(
    val postalCode: String
) : Exception()
