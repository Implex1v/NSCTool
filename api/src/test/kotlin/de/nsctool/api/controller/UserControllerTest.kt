package de.nsctool.api.controller

import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.exceptions.BadRequestException
import de.nsctool.api.exceptions.NotFoundException
import de.nsctool.api.exceptions.UnauthorizedException
import de.nsctool.api.model.User
import de.nsctool.api.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID
import javax.servlet.http.HttpServletRequest

internal class UserControllerTest {
    private val repository = spyk<UserRepository>()
    private val client = spyk<KeycloakClient>()
    private val controller = UserController(client, repository)
    private val user = UserController.CreateUserRequest("user", "user@example.com", "123456")
    private val request = spyk<HttpServletRequest>()
    private val auth = mockk<JwtAuthenticationToken>()
    private val uuid = UUID.randomUUID()

    @Test
    fun `should create user`() {
        val uuid = UUID.randomUUID()
        every { client.createUser(user.username, user.email, user.password, listOf(Role.USER)) } returns uuid
        justRun { client.resetPassword(uuid, user.password, false) }
        justRun { client.addRealmRoleToUser(uuid, Role.USER) }
        every { repository.save(any()) } returns User().apply {
            userName = user.username
            id = uuid
            email = user.email
        }

        controller.create(user)

        verify { client.createUser(user.username, user.email, user.password, listOf(Role.USER)) }
        verify { client.resetPassword(uuid, user.password, false) }
        verify { repository.save(any()) }
    }

    @Test
    fun `should handle create user error`() {
        every { client.createUser(user.email, user.username, user.password, listOf(Role.USER)) } throws(BadRequestException("Foo"))
        shouldThrowExactly<BadRequestException> { controller.create(user) }
    }

    @Test
    fun `should handle user deletion`() {
        every { request.userPrincipal } returns auth
        every { auth.name } returns "foo"

        shouldThrowExactly<UnauthorizedException> { controller.delete(request, uuid.toString()) }

        every { auth.authorities } returns listOf(SimpleGrantedAuthority(Role.USER.value))
        shouldThrowExactly<UnauthorizedException> { controller.delete(request, uuid.toString()) }

        every { auth.authorities } returns listOf(SimpleGrantedAuthority(Role.ADMIN.value))
        justRun { client.deleteUser(uuid) }
        justRun { repository.deleteById(uuid) }
        controller.delete(request, uuid.toString())

        every { repository.deleteById(uuid) } throws EmptyResultDataAccessException(5)
        shouldThrowExactly<NotFoundException> { controller.delete(request, uuid.toString()) }
    }
}