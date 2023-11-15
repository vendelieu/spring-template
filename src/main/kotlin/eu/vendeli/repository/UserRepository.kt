package eu.vendeli.repository

import eu.vendeli.dto.request.UserUpdateRequest
import eu.vendeli.jooq.generated.Tables.USER
import eu.vendeli.jooq.generated.tables.daos.UserDao
import eu.vendeli.jooq.generated.tables.pojos.User
import org.springframework.stereotype.Repository

@Repository
class UserRepository : UserDao() {
    fun update(email: String, user: UserUpdateRequest): User? = dslContext.newRecord(table, user).let {
        dslContext.update(table).set(it)
            .where(USER.EMAIL.eq(email)).returningResult()
            .fetchOneInto(User::class.java)
    }
}