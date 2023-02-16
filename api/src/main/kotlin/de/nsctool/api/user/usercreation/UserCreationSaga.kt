package de.nsctool.api.user.usercreation

import de.nsctool.api.choreography.WorkflowManager
import de.nsctool.api.user.usercreation.steps.AddRealmRoleStep
import de.nsctool.api.user.usercreation.steps.CreateUserKeycloakStep
import de.nsctool.api.user.usercreation.steps.ResetPasswordStep
import de.nsctool.api.user.usercreation.steps.SaveUserStep
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class UserCreationSaga(
    createUserKeycloakStep: CreateUserKeycloakStep,
    resetPasswordStep: ResetPasswordStep,
    addRealmRoleStep: AddRealmRoleStep,
    saveUserStep: SaveUserStep,
    meterRegistry: MeterRegistry,
): WorkflowManager<UserCreationData>(
    meterRegistry,
    listOf(
        createUserKeycloakStep,
        resetPasswordStep,
        addRealmRoleStep,
        saveUserStep,
    )
)
