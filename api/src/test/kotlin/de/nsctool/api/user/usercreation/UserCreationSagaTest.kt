package de.nsctool.api.user.usercreation

import de.nsctool.api.choreography.WorkflowRecoverFailedException
import de.nsctool.api.choreography.WorkflowRecoverSuccessException
import de.nsctool.api.user.usercreation.steps.AddRealmRoleStep
import de.nsctool.api.user.usercreation.steps.CreateUserKeycloakStep
import de.nsctool.api.user.usercreation.steps.ResetPasswordStep
import de.nsctool.api.user.usercreation.steps.SaveUserStep
import io.kotest.assertions.throwables.shouldThrow
import io.micrometer.core.instrument.Metrics
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserCreationSagaTest {
    private val createUserKeycloakStep = mockk<CreateUserKeycloakStep>()
    private val resetPasswordStep = mockk<ResetPasswordStep>()
    private val addRealmRoleStep = mockk<AddRealmRoleStep>()
    private val saveUserStep = mockk<SaveUserStep>()
    private val registry = Metrics.globalRegistry
    private val data = mockk<UserCreationData>()

    private val saga = UserCreationSaga(createUserKeycloakStep, resetPasswordStep, addRealmRoleStep, saveUserStep, registry)

    @BeforeEach
    fun setup() {
        every { createUserKeycloakStep.status() } returns UserCreationStatus.CREATE_USER_KEYCLOAK
        every { resetPasswordStep.status() } returns UserCreationStatus.RESET_PASSWORD
        every { addRealmRoleStep.status() } returns UserCreationStatus.ADD_REALM_ROLE
        every { saveUserStep.status() } returns UserCreationStatus.SAVE_USER
    }

    @Test
    fun shouldRunSaga() {
        every { createUserKeycloakStep.process(data) } returns data
        every { resetPasswordStep.process(data) } returns data
        every { addRealmRoleStep.process(data) } returns data
        every { saveUserStep.process(data) } returns data

        saga.run(data)

        verifyOrder {
            createUserKeycloakStep.process(data)
            resetPasswordStep.process(data)
            addRealmRoleStep.process(data)
            saveUserStep.process(data)
        }
    }

     @Test
     fun shouldRecoverSaveUserStep() {
         every { createUserKeycloakStep.process(data) } returns data
         every { resetPasswordStep.process(data) } returns data
         every { addRealmRoleStep.process(data) } returns data
         every { saveUserStep.process(data) }.throws(Exception())
         every { createUserKeycloakStep.revert(data) } returns data
         every { resetPasswordStep.revert(data) } returns data
         every { addRealmRoleStep.revert(data) } returns data
         every { saveUserStep.revert(data) } returns data

         shouldThrow<WorkflowRecoverSuccessException> {
             saga.run(data)
         }

         verifyOrder {
             createUserKeycloakStep.process(data)
             resetPasswordStep.process(data)
             addRealmRoleStep.process(data)
             saveUserStep.process(data)
             saveUserStep.revert(data)
             addRealmRoleStep.revert(data)
             resetPasswordStep.revert(data)
             createUserKeycloakStep.revert(data)
         }
     }

    @Test
    fun shouldRecoverAddRealmRole() {
        every { createUserKeycloakStep.process(data) } returns data
        every { resetPasswordStep.process(data) } returns data
        every { addRealmRoleStep.process(data) }.throws(Exception())
        every { createUserKeycloakStep.revert(data) } returns data
        every { resetPasswordStep.revert(data) } returns data
        every { addRealmRoleStep.revert(data) } returns data

        shouldThrow<WorkflowRecoverSuccessException> {
            saga.run(data)
        }

        verifyOrder {
            createUserKeycloakStep.process(data)
            resetPasswordStep.process(data)
            addRealmRoleStep.process(data)
            addRealmRoleStep.revert(data)
            resetPasswordStep.revert(data)
            createUserKeycloakStep.revert(data)
        }
    }

    @Test
    fun shouldRecoverResetPassword() {
        every { createUserKeycloakStep.process(data) } returns data
        every { resetPasswordStep.process(data) }.throws(Exception())
        every { createUserKeycloakStep.revert(data) } returns data
        every { resetPasswordStep.revert(data) } returns data

        shouldThrow<WorkflowRecoverSuccessException> {
            saga.run(data)
        }

        verifyOrder {
            createUserKeycloakStep.process(data)
            resetPasswordStep.process(data)
            resetPasswordStep.revert(data)
            createUserKeycloakStep.revert(data)
        }
    }

    @Test
    fun shouldRecoverCreateUserKeycloak() {
        every { createUserKeycloakStep.process(data) }.throws(Exception())
        every { createUserKeycloakStep.revert(data) } returns data

        shouldThrow<WorkflowRecoverSuccessException> {
            saga.run(data)
        }

        verifyOrder {
            createUserKeycloakStep.process(data)
            createUserKeycloakStep.revert(data)
        }
    }

    @Test
    fun shouldNotRecover() {
        every { createUserKeycloakStep.process(data) }.throws(Exception())
        every { createUserKeycloakStep.revert(data) }.throws(Exception())

        shouldThrow<WorkflowRecoverFailedException> {
            saga.run(data)
        }

        verifyOrder {
            createUserKeycloakStep.process(data)
            createUserKeycloakStep.revert(data)
            createUserKeycloakStep.revert(data)
            createUserKeycloakStep.revert(data)
        }
    }
}