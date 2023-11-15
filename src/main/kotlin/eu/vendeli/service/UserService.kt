package eu.vendeli.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import eu.vendeli.dto.request.UserUpdateRequest
import eu.vendeli.jooq.generated.tables.daos.UserDao
import eu.vendeli.jooq.generated.tables.pojos.User
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userDao: UserDao
) {
    fun create(block: User.() -> Unit): User = userDao.create(block)

    fun get(id: Long): User? = userDao.fetchOneById(id)

    fun get(email: String): User = userDao.fetchOneByEmail(email) ?: create { username = email }

    sealed class UserUpdateFailures {
        data object UserNotFound : UserUpdateFailures()
        data object DataUpdateFailure : UserUpdateFailures()
    }

    fun update(email: String, updateUser: UserUpdateRequest): Either<UserUpdateFailures, User> {
        val user = userDao.fetchOneByEmail(email) ?: return UserUpdateFailures.UserNotFound.left()
        user.apply {
            username = updateUser.username
            name = updateUser.name
        }
        userDao.runCatching { update(user) }.getOrElse {
            UserUpdateFailures.DataUpdateFailure.left()
        }

        return user.right()
    }
}