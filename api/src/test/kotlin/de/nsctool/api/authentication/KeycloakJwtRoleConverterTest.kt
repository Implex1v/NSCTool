package de.nsctool.api.authentication

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt

internal class KeycloakJwtRoleConverterTest {
    private val converter = KeycloakJwtRoleConverter()

    @Test
    fun `should convert JWT Roles`() {
        val roleName = "ROLE_foo-bar"
        val jwt = Jwt
            .withTokenValue("fooo")
            .header("foo", "bar")
            .claim("roles", listOf(roleName))
            .build()

        val result = converter.convert(jwt)

        result.authorities.size shouldBe 1
        result.authorities.first().authority shouldBe roleName
    }

    @Test
    fun `should convert empty roles`() {
        val jwt = Jwt
            .withTokenValue("fooo")
            .header("foo", "bar")
            .claim("roles", emptyList<String>())
            .build()

        val result = converter.convert(jwt)

        result.authorities.size shouldBe 0
    }
}