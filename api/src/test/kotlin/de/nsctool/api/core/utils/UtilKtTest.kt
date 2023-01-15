package de.nsctool.api.core.utils

import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.core.controller.getUsername
import de.nsctool.api.core.controller.hasUserRole
import de.nsctool.api.core.controller.isUser
import de.nsctool.api.core.controller.parseUUIDOrThrow
import de.nsctool.api.core.exceptions.BadRequestException
import de.nsctool.api.core.exceptions.UnauthorizedException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

internal class UtilKtTest {
    private val request = spyk<HttpServletRequest>()
    private val auth = mockk<JwtAuthenticationToken>()

    @Test
    fun `hasUserRole() should check for role`() {
        every { request.userPrincipal } returns auth
        every { auth.authorities } returns listOf(
            SimpleGrantedAuthority(Role.USER.value)
        )

        request.hasUserRole(Role.USER) shouldBe true

        every { auth.authorities } returns listOf(
            SimpleGrantedAuthority(Role.ADMIN.value)
        )

        request.hasUserRole(Role.USER) shouldBe false
    }

    @Test
    fun `getUsername() should return username if any`() {
        every { request.userPrincipal } returns auth
        every { auth.name } returns "Foo"
        request.getUsername() shouldBe "Foo"

        every { auth.name } returns null
        shouldThrowExactly<UnauthorizedException> { request.getUsername() }
    }

    @Test
    fun `isUser() should return if given user is current user`() {
        every { request.userPrincipal } returns auth
        val user = UUID.randomUUID().toString()
        every { auth.name } returns user.toString()

        request.isUser(user) shouldBe true

        request.isUser(UUID.randomUUID().toString()) shouldBe false
    }

    @Test
    fun `parseUUIDOrThrow() should parse string as UUID or throw`() {
        val uuid = UUID.randomUUID()

        uuid.toString().parseUUIDOrThrow() shouldBe uuid

        shouldThrowExactly<BadRequestException> { "Foo".parseUUIDOrThrow() }
    }
}