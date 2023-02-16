package de.nsctool.api.user.usercreation

import de.nsctool.api.choreography.WorkflowStatus

enum class UserCreationStatus: WorkflowStatus {
    CREATE_USER_KEYCLOAK, RESET_PASSWORD, ADD_REALM_ROLE, SAVE_USER
}
