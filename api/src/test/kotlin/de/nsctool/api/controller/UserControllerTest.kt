package de.nsctool.api.controller

import de.nsctool.api.authentication.keycloak.ApiRoles
import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.controller.exceptions.BadRequestException
import de.nsctool.api.model.User
import de.nsctool.api.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID

internal class UserControllerTest {
    private val repository = spyk<UserRepository>()
    private val client = spyk<KeycloakClient>()
    private val controller = UserController(client, repository)
    private val user = UserController.CreateUserRequest("user", "user@example.com", "123456")

    @Test
    fun `should create user`() {
        val uuid = UUID.randomUUID()
        every { client.createUser(user.username, user.email, user.password, listOf(ApiRoles.USER)) } returns uuid.toString()
        justRun { client.resetPassword(uuid.toStr(), user.password, false) }
        every { repository.save(any()) } returns User().apply {
            userName = user.username
            id = uuid
            email = user.email
        }

        controller.create(user)

        verify { client.createUser(user.username, user.email, user.password, listOf(ApiRoles.USER)) }
        verify { client.resetPassword(uuid.toStr(), user.password, false) }
        verify { repository.save(any()) }
    }

    @Test
    fun `should handle create user error`() {
        every { client.createUser(user.email, user.username, user.password, listOf(ApiRoles.USER)) } throws(BadRequestException("Foo"))
        shouldThrowExactly<BadRequestException> { controller.create(user) }
    }
}