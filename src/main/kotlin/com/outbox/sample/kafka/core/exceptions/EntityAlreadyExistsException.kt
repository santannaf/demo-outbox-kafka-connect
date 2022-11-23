package com.outbox.sample.kafka.core.exceptions

import kotlin.reflect.KClass

class EntityAlreadyExistsException(
    val entityClass: KClass<*>,
    val entityId: Any,
    val searchValue: Any,
    val searchField: String
) : Exception()
