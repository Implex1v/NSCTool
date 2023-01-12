package de.nsctool.api.user

import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.core.controller.hasUserRole
import de.nsctool.api.core.controller.isUser
import de.nsctool.api.core.controller.parseUUIDOrThrow
import de.nsctool.api.core.exceptions.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    val service: UserService
) {
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping
    @ResponseBody
    //@Secured(value = [Roles.ROLE_MGMT])
    fun create(@RequestBody apiUser: CreateUserRequest): User {
        val user = User(userName = apiUser.username, email = apiUser.email)
        return service.create(user, apiUser.password)
    }

    @DeleteMapping("/{id}")
    fun delete(request: HttpServletRequest, @PathVariable id: String) {
        val uuid = id.parseUUIDOrThrow()
        if(!request.isUser(uuid) && !request.hasUserRole(Role.ADMIN)) {
            throw UnauthorizedException("No permissions to delete user")
        }

        service.delete(uuid)
    }

    class CreateUserRequest(val username: String, val email: String, val password: String)
}