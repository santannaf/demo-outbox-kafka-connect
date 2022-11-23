package com.outbox.sample.kafka.core.user

import com.outbox.sample.kafka.GlobalExceptionHandler
import com.outbox.sample.kafka.core.exceptions.EntityAlreadyExistsException
import com.outbox.sample.kafka.core.exceptions.UnknownPostalCodeException
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid
import kotlin.math.roundToInt

@RestController
class UserResource(
    private val userCreateService: UserCreateService
) {
    @GetMapping(path = ["/users/random"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createRandomUser(
        @RequestParam(
            name = "emailDomain",
            required = false
        ) emailDomain: String?
    ): UserCreateResponse {
        val name: String = RandomStringUtils.randomAlphabetic(8)
        val userCreateForm = UserCreateForm(
            email = String.format(
                "%s@%s.com", name,
                Optional.ofNullable(emailDomain).orElse(RandomStringUtils.randomAlphabetic(5))
            ),
            addressPostalCode = RandomStringUtils.randomNumeric(8),
            name = name,
            age = ((18 + (Math.random() * 30)).roundToInt())
        )

        return createUser(userCreateForm)
    }

    @PostMapping(
        path = ["/users"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createUser(@Valid @RequestBody userCreateForm: UserCreateForm): UserCreateResponse {
        val createdUser: UserEntity = try {
            userCreateService.createUser(
                userCreateForm.email, userCreateForm.name,
                userCreateForm.age, userCreateForm.addressPostalCode
            )
        } catch (e: EntityAlreadyExistsException) {
            throw GlobalExceptionHandler.BadRequestException(
                String.format(
                    "Usuário com %s %s ja cadastrado com id %s", e.searchField,
                    e.searchValue, e.entityId
                )
            )
        } catch (e: UnknownPostalCodeException) {
            throw GlobalExceptionHandler.BadRequestException(
                String.format(
                    "Postal code %s não existente no estado de São Paulo",
                    e.postalCode
                )
            )
        }
        return UserCreateResponse(createdUser)
    }
}
