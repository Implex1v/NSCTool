package de.nsctool.api.user.usercreation.steps

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.user.User
import de.nsctool.api.user.usercreation.UserCreationData
import de.nsctool.api.user.usercreation.UserCreationStatus
import io.kotest.matchers.shouldBe
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ResetPasswordStepTest {
    private val data = UserCreationData(User("id", "username", "email"), "password", Role.USER, "id")
    private val client = mockk<KeycloakClient>()
    private val step = ResetPasswordStep(client)

    @Test
    fun shouldGetStatus() {
        step.status() shouldBe UserCreationStatus.RESET_PASSWORD
    }

    @Test
    fun shouldProcess() {
        justRun { client.resetPassword(any(), any(), any()) }

        step.process(data) shouldBe data

        verify { client.resetPassword(data.userId!!, data.password, false) }
    }

    @Test
    fun shouldProcessIfIdNull() {
        data.userId = null

        assertThrows<IllegalStateException> {
            step.process(data)
        }
    }
}