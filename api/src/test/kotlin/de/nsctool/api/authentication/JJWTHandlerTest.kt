package de.nsctool.api.authentication

import de.nsctool.api.model.User
import de.nsctool.api.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

internal class JJWTHandlerTest {
    private val repository = mockk<UserRepository>()
    private val handler = JJWTHandler(repository)
    private val id = UUID.randomUUID()
    private val user = mockk<User>()

    @Test
    fun shouldHandleJwt() {
        val id = UUID.randomUUID()
        every { user.id } returns id
        every { repository.findById(id) } returns Optional.of(user)

        val token = handler.generateToken(user)
        val actualUser = handler.parseToken(token)

        actualUser.shouldBe(user)
    }

    @Test
    fun shouldParseTokenErrors() {
        every { user.id } returns id
        every { repository.findById(any()) } returns Optional.empty()

        shouldThrowExactly<AuthenticationException> {
            handler.parseToken("foo")
        }

        shouldThrowExactly<AuthenticationException> {
            handler.parseToken(handler.generateToken(user))
        }
    }
}