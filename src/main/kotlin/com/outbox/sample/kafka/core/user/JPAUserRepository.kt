package com.outbox.sample.kafka.core.user

import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
@Transactional
class JPAUserRepository(
    @PersistenceContext
    private val entityManager: EntityManager
) : UserRepository {

    fun createUser(email: String, name: String, age: Int, addressPostalCode: String): UserEntity {
        val entity = UserEntity(email = email, name = name, age = age, addressPostalCode = addressPostalCode)
        entityManager.persist(entity)
        return entity
    }

    override fun findUserByEmail(email: String): UserEntity? {
        val hql = StringBuilder()
        hql.append("select u from ")
        hql.append("User")
        hql.append(" u where u.email = :email")

        val users = entityManager.createQuery(
            hql.toString(),
            UserEntity::class.java
        )
            .setParameter("email", email).resultList

        if (users.isEmpty()) return null

        check(users.size != 1) { "Mais de um usuário com mesmo email" }

        return users[0]
    }
}