package de.nsctool.api.user

import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.core.exceptions.BadRequestException
import de.nsctool.api.core.exceptions.UnauthorizedException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

internal class UserControllerTest {
    private val service = mockk<UserService>()
    private val controller = UserController(service)
    private val user = UserController.CreateUserRequest("user", "user@example.com", "123456")
    private val entity = User(userName = user.username, email = user.email, id = UUID.randomUUID().toString())
    private val request = spyk<HttpServletRequest>()
    private val auth = mockk<JwtAuthenticationToken>()
    private val uuid = UUID.randomUUID().toString()

    @Test
    fun `should create user`() {
        every { service.create(any(), any()) } returns entity

        val actualUser = controller.create(user)

        verify { service.create(any(), user.password) }

        actualUser.userName shouldBe entity.userName
        actualUser.email shouldBe entity.email
        actualUser.id shouldNotBe null
    }

    @Test
    fun `should handle create user error`() {
        every {
            service.create(any(), any())
        } throws(BadRequestException("Foo"))

        shouldThrowExactly<BadRequestException> { controller.create(user) }
        verify { service.create(any(), user.password) }
    }

    @Test
    fun `should handle user deletion`() {
        every { request.userPrincipal } returns auth
        every { auth.name } returns "foo"

        shouldThrowExactly<UnauthorizedException> { controller.delete(request, uuid) }

        every { auth.authorities } returns listOf(SimpleGrantedAuthority(Role.USER.value))
        shouldThrowExactly<UnauthorizedException> { controller.delete(request, uuid) }

        every { auth.authorities } returns listOf(SimpleGrantedAuthority(Role.ADMIN.value))
        justRun { service.delete(uuid) }
        controller.delete(request, uuid)

        every { service.delete(uuid) } throws EmptyResultDataAccessException(5)
        shouldThrowExactly<EmptyResultDataAccessException> { controller.delete(request, uuid) }
    }
}