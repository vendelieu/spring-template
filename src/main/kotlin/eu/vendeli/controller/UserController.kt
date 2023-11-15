package eu.vendeli.controller

import eu.vendeli.dto.request.UserUpdateRequest
import eu.vendeli.dto.wrapper.Response
import eu.vendeli.dto.wrapper.failureResponse
import eu.vendeli.dto.wrapper.successResponse
import eu.vendeli.jooq.generated.tables.pojos.User
import eu.vendeli.service.UserService
import eu.vendeli.service.UserService.UserUpdateFailures
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    value = ["/api/v1/user"],
    produces = ["application/json"]
)
class UserController(
    private val userService: UserService
) {
    @GetMapping("/")
    fun get(auth: JwtAuthenticationToken): Response<User> =
        successResponse { userService.get(auth.name) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Response<User?> = successResponse {
        userService.get(id)
    }

    @PostMapping("/")
    fun update(
        @RequestBody user: UserUpdateRequest,
        auth: JwtAuthenticationToken
    ) = userService.update(auth.name, user).fold({
        when (it) {
            UserUpdateFailures.DataUpdateFailure -> failureResponse { "Problems during the user profile update process." }
            UserUpdateFailures.UserNotFound -> failureResponse { "User not found." }
        }
    }, {
        successResponse { it }
    })
}