package de.nsctool.api.user

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.core.exceptions.InternalServerErrorException
import de.nsctool.api.user.usercreation.UserCreationData
import de.nsctool.api.user.usercreation.UserCreationSaga
import org.springframework.stereotype.Service

@Service
class UserService(
    val client: KeycloakClient,
    val repository: UserRepository,
    val saga: UserCreationSaga,
) {
    fun create(user: User, password: String): User {
        try {
            saga.run(UserCreationData(user, password))
            return user
        } catch (ex: Exception) {
            throw InternalServerErrorException("Failed to create user", ex)
        }
    }

    fun delete(id: String) {
        client.deleteUser(id)
        repository.deleteById(id)
    }
}