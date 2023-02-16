package de.nsctool.api.user.usercreation.steps

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.choreography.WorkflowStep
import de.nsctool.api.user.usercreation.UserCreationData
import de.nsctool.api.user.usercreation.UserCreationStatus
import org.springframework.stereotype.Component

@Component
class ResetPasswordStep(
    private val client: KeycloakClient,
): WorkflowStep<UserCreationData> {
    override fun status() = UserCreationStatus.RESET_PASSWORD

    override fun process(data: UserCreationData): UserCreationData {
        val userId = data.userId ?: error("user id is null")

        client.resetPassword(
            userId = userId,
            password = data.password,
            temporary = false
        )
        return data
    }

    override fun revert(data: UserCreationData): UserCreationData {
        // Nothing to do since user has no password after creation
        return data
    }
}