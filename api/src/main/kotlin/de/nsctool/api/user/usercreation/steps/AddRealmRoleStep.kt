package de.nsctool.api.user.usercreation.steps

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.choreography.WorkflowStatus
import de.nsctool.api.choreography.WorkflowStep
import de.nsctool.api.user.usercreation.UserCreationData
import de.nsctool.api.user.usercreation.UserCreationStatus
import org.springframework.stereotype.Component

@Component
class AddRealmRoleStep(
    val client: KeycloakClient,
): WorkflowStep<UserCreationData> {
    override fun status(): WorkflowStatus = UserCreationStatus.ADD_REALM_ROLE

    override fun process(data: UserCreationData): UserCreationData {
        val userId = data.userId ?: error("user id is null")
        client.addRealmRoleToUser(userId = userId, roleName = data.roleName)
        return data
    }

    override fun revert(data: UserCreationData): UserCreationData {
        // TODO
        return data
    }
}