package de.nsctool.api.user.usercreation.steps

import de.nsctool.api.choreography.WorkflowStep
import de.nsctool.api.user.UserRepository
import de.nsctool.api.user.usercreation.UserCreationData
import de.nsctool.api.user.usercreation.UserCreationStatus
import org.springframework.stereotype.Component

@Component
class SaveUserStep(
    val repository: UserRepository,
): WorkflowStep<UserCreationData> {
    override fun status() = UserCreationStatus.SAVE_USER

    override fun process(data: UserCreationData): UserCreationData {
        data.user.copy(
            id = data.userId ?: error("user id is null"),
            userName = data.user.userName,
            email = data.user.email,
        ).let {
            repository.save(it)
            data.user = it
        }
        return data
    }

    override fun revert(data: UserCreationData): UserCreationData {
        repository.deleteById(data.userId ?: error("user id is null"))
        return data
    }
}