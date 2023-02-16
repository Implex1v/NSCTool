package de.nsctool.api.user.usercreation.steps

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.user.usercreation.UserCreationData
import de.nsctool.api.user.usercreation.UserCreationStatus
import io.kotest.matchers.shouldBe
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class AddRealmRoleStepTest {
    private val client = mockk<KeycloakClient>()
    private val step = AddRealmRoleStep(client)

    @Test
    fun shouldReturnStatus() {
        step.status() shouldBe UserCreationStatus.ADD_REALM_ROLE
    }

    @Test
    fun shouldProcess() {
        val userData = UserCreationData(mockk(), password = "abc", userId = "id", roleName = Role.MANAGEMENT)
        justRun { client.addRealmRoleToUser(any(), any()) }

        step.process(userData) shouldBe userData

        verify { client.addRealmRoleToUser(userData.userId!!, userData.roleName) }
    }

    @Test
    fun shouldRevert() {
        val userData = UserCreationData(mockk(), password = "abc", userId = "id", roleName = Role.MANAGEMENT)
        step.revert(userData) shouldBe userData
    }
}