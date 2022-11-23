package com.outbox.sample.kafka.core.user

import com.outbox.sample.kafka.core.event.EventRepository
import com.outbox.sample.kafka.core.exceptions.EntityAlreadyExistsException
import com.outbox.sample.kafka.core.exceptions.UnknownPostalCodeException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import web.core.user.pub.UserChangedMessage
import javax.transaction.Transactional

@Service
@Transactional(rollbackOn = [Exception::class])
class UserCreateService(
    private val userRepository: JPAUserRepository,
    private val eventRepository: EventRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Throws(EntityAlreadyExistsException::class, UnknownPostalCodeException::class)
    fun createUser(email: String, name: String, age: Int, addressPostalCode: String): UserEntity {
        log.info("Criando usuário {}", email)

        val exist = userRepository.findUserByEmail(email)
        if (exist != null) throw EntityAlreadyExistsException(UserEntity::class, exist.id, "email", email)

        log.info("Persistindo usuário {}", email)
        val user = userRepository.createUser(email, name, age, addressPostalCode)

        log.info("Criando evento de criação de usuário")

        val message = UserChangedMessage.newBuilder()
            .setUserId(user.id.toString())
            .setUserEmail(user.email)
            .setUserName(user.name)
            .build()

        eventRepository.createEvent("user.changed.kconnect", user.id.toString(), message)

        if (user.email.indexOf("hotmail") > 0) {
            throw RuntimeException()
        }
        return user
    }
}