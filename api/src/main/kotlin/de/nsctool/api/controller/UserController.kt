package de.nsctool.api.controller

import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.authentication.keycloak.Roles
import de.nsctool.api.controller.exceptions.BadRequestException
import de.nsctool.api.model.User
import de.nsctool.api.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
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
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping
    @ResponseBody
    @Secured(value = [Roles.ROLE_MGMT])
    fun create(@RequestBody user: CreateUserRequest): User {
        try {
            val userId = client.createUser(user.username, user.email, user.password, listOf(Role.USER))
            client.resetPassword(userId, user.password, false)
            logger.info("Created user '$userId' ('${user.username}')")
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