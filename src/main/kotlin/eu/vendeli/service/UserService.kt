package eu.vendeli.service

import eu.vendeli.dto.request.UserUpdateRequest
import eu.vendeli.jooq.generated.tables.pojos.User
import eu.vendeli.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun create(block: User.() -> Unit): User = userRepository.create(block)

    fun get(id: Long): User? = userRepository.fetchOneById(id)

    fun get(email: String): User = userRepository.fetchOneByEmail(email) ?: create { username = email }

    fun update(email: String, user: UserUpdateRequest) = userRepository.update(email, user)
}