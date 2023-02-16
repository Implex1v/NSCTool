package de.nsctool.api.user.usercreation.steps

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.user.User
import de.nsctool.api.user.usercreation.UserCreationData
import de.nsctool.api.user.usercreation.UserCreationStatus
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class CreateUserKeycloakStepTest {
    private val client = mockk<KeycloakClient>()
    private val step = CreateUserKeycloakStep(client)
    private val data = UserCreationData(User("", "user", "foo@bar.com"), "password", Role.USER, "userid")
    private val id = "id"

    @Test
    fun shouldHaveCorrectStatus() {
        step.status() shouldBe UserCreationStatus.CREATE_USER_KEYCLOAK
    }

    @Test
    fun shouldProcess() {
        every { client.createUser(any(), any(), any()) } returns id

        step.process(data).userId shouldBe id

        verify { client.createUser(data.user.userName, data.user.email, listOf(Role.USER)) }
    }

    @Test
    fun shouldRevert() {
        justRun { client.deleteUser(any()) }

        step.revert(UserCreationData(data.user, "", userId = id))

        verify { client.deleteUser(id) }
    }

    @Test
    fun shouldRevertIfIdIsNull() {
        step.revert(UserCreationData(data.user, ""))
    }
}