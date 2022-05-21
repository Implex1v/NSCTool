package de.nsctool.api.controller

import de.nsctool.api.authentication.keycloak.ApiRoles
import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.controller.exceptions.BadRequestException
import de.nsctool.api.model.User
import de.nsctool.api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    @Autowired val client: KeycloakClient,
    @Autowired val repository: UserRepository,
) {
    @PostMapping
    @ResponseBody
    fun create(@RequestBody user: CreateUserRequest): User {
        try {
            val userId = client.createUser(user.username, user.email, user.password, listOf(ApiRoles.USER))
            client.resetPassword(userId, user.password, false)
            return User().apply {
                id = userId.parseUUIDOrThrow()
                userName = user.username
                email = user.email
                repository.save(this)
            }
        } catch (ex: Exception) {
            throw BadRequestException("Failed to create user", ex)
        }
    }

    class CreateUserRequest(val username: String, val email: String, val password: String)
}