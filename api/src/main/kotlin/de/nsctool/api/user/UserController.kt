package de.nsctool.api.user

import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.core.controller.hasUserRole
import de.nsctool.api.core.controller.isUser
import de.nsctool.api.core.controller.parseUUIDOrThrow
import de.nsctool.api.core.exceptions.BadRequestException
import de.nsctool.api.core.exceptions.NotFoundException
import de.nsctool.api.core.exceptions.UnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/users")
class UserController(
    @Autowired val client: KeycloakClient,
    @Autowired val repository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping
    @ResponseBody
    //@Secured(value = [Roles.ROLE_MGMT])
    fun create(@RequestBody user: CreateUserRequest): User {
        try {
            val userId = client
                .createUser(user.username, user.email, user.password, listOf(Role.USER))

            client.resetPassword(userId, user.password, false)
            client.addRealmRoleToUser(userId, Role.USER)
            logger.info("Created user '$userId' ('${user.username}')")
            return User().apply {
                id = userId
                userName = user.username
                email = user.email
                repository.save(this)
            }
        } catch (ex: Exception) {
            throw BadRequestException("Failed to create user", ex)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(request: HttpServletRequest, @PathVariable id: String) {
        val uuid = id.parseUUIDOrThrow()
        if(!request.isUser(uuid) && !request.hasUserRole(Role.ADMIN)) {
            throw UnauthorizedException("No permissions to delete user")
        }

        client.deleteUser(uuid)

        try {
            repository.deleteById(uuid)
        } catch (ex: EmptyResultDataAccessException) {
            throw NotFoundException("User not found", ex)
        }
    }

    class CreateUserRequest(val username: String, val email: String, val password: String)
}