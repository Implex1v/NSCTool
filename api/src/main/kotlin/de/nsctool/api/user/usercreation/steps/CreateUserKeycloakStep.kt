package de.nsctool.api.user.usercreation.steps

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.choreography.WorkflowStatus
import de.nsctool.api.choreography.WorkflowStep
import de.nsctool.api.user.usercreation.UserCreationData
import de.nsctool.api.user.usercreation.UserCreationStatus
import org.springframework.stereotype.Component

@Component
class CreateUserKeycloakStep(
    private val client: KeycloakClient,
): WorkflowStep<UserCreationData> {
    override fun status(): WorkflowStatus = UserCreationStatus.CREATE_USER_KEYCLOAK

    override fun process(data: UserCreationData): UserCreationData {
        val userId = client.createUser(
            username = data.user.userName,
            email = data.user.email,
            roles = listOf(Role.USER)
        )

        return data.apply {
            this.userId = userId
        }
    }

    override fun revert(data: UserCreationData): UserCreationData {
        if (data.userId == null) {
            return data
        }
        client.deleteUser(data.userId!!)
        return data
    }
}