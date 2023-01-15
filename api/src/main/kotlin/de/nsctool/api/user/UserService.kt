package de.nsctool.api.user

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.core.exceptions.InternalServerErrorException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(
    val client: KeycloakClient,
    val repository: UserRepository,
) {
    private val logger by lazy { LoggerFactory.getLogger(UserService::class.java) }

    fun create(user: User, password: String): User {
        try {
            val userId = client.createUser(
                username = user.userName,
                email = user.email,
                roles = listOf(Role.USER)
            )

            client.resetPassword(userId = userId, password = password, temporary = false)
            client.addRealmRoleToUser(userId = userId, roleName = Role.USER)
            logger.info("Created user '$userId' ('${user.userName}')")
            return user.copy(
                id = userId,
                userName = user.userName,
                email = user.email,
            ).let {
                repository.save(it)
            }
        } catch (ex: Exception) {
            throw InternalServerErrorException("Failed to create user", ex)
        }
    }

    fun delete(id: String) {
        client.deleteUser(id)
        repository.deleteById(id)
    }
}