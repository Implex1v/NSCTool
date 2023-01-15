package de.nsctool.api.user

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.core.exceptions.BadRequestException
import de.nsctool.api.core.exceptions.InternalServerErrorException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.*
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

class UserServiceTest {
    private val client = mockk<KeycloakClient>()
    private val repository = mockk<UserRepository>()
    private val service = UserService(client = client, repository = repository)
    private val user = User(userName = "username", email = "f@foo.bar", id ="")
    private val password = "123456"
    private val request = spyk<HttpServletRequest>()
    private val auth = mockk<JwtAuthenticationToken>()
    private val uuid = UUID.randomUUID().toString()

    @Test
    fun `should create user`() {
        every { client.createUser(user.userName, user.email, listOf(Role.USER)) } returns uuid
        justRun { client.resetPassword(uuid, password, false) }
        justRun { client.addRealmRoleToUser(uuid, Role.USER) }
        every { repository.save(any()) } returns User(
            userName = user.userName,
            id = uuid,
            email = user.email,
        )

        service.create(user = user, password = password)

        verify { client.createUser(user.userName, user.email, listOf(Role.USER)) }
        verify { client.resetPassword(uuid, password, false) }
        verify { repository.save(any()) }
    }

    @Test
    fun `should handle create user error`() {
        every {
            client.createUser(
                user.email,
                user.userName,
                listOf(Role.USER)
            )
        } throws(BadRequestException("Foo"))
        shouldThrowExactly<InternalServerErrorException> { service.create(user = user, password = password) }
    }

    @Test
    fun `should handle user deletion`() {
        justRun { client.deleteUser(uuid) }
        justRun { repository.deleteById(uuid) }
        service.delete(id = uuid)

        every { client.deleteUser(uuid) } throws(BadRequestException("ex"))
        shouldThrowExactly<BadRequestException> { service.delete(uuid) }
    }
}