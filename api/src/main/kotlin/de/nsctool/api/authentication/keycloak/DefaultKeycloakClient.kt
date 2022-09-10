package de.nsctool.api.authentication.keycloak

import de.nsctool.api.exceptions.BadRequestException
import de.nsctool.api.controller.parseUUIDOrThrow
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class DefaultKeycloakClient: KeycloakClient {
    @Value("\${keycloak.admin.baseUri}")
    private lateinit var baseUri: String

    @Value("\${keycloak.admin.realm}")
    private lateinit var realm: String

    @Value("\${keycloak.admin.username}")
    private lateinit var username: String

    @Value("\${keycloak.admin.password}")
    private lateinit var password: String

    @Value("\${keycloak.admin.client.id}")
    private lateinit var clientId: String

    @Value("\${keycloak.admin.client.secret}")
    private lateinit var clientSecret: String

    private val client: RealmResource by lazy {
        Keycloak
            .getInstance(baseUri, realm, username, password, clientId, clientSecret)
            .realm(realm)
    }

    override fun createUser(
        username: String,
        email: String,
        password: String,
        roles: List<Role>): UUID
    {
        try {
            val userResource = client.users()
            val user = UserRepresentation().apply {
                isEnabled = true
                setUsername(username)
                setEmail(email)
                realmRoles = roles.map { it.value }
            }

            val userId = userResource.create(user).let {
                CreatedResponseUtil.getCreatedId(it).parseUUIDOrThrow()
            }

            return userId
        } catch (ex: Exception) {
            // sadly we don't know what exception is thrown on error => diaper pattern
            throw BadRequestException("Failed to register user", ex)
        }
    }

    override fun resetPassword(userId: UUID, password: String, temporary: Boolean) {
        try {
            val passwordReset = CredentialRepresentation().apply {
                isTemporary = temporary
                type = CredentialRepresentation.PASSWORD
                value = password
            }

            val user = client
                .users()
                .get(userId.toString())

            user.resetPassword(passwordReset)
        } catch (ex: Exception) {
            throw BadRequestException("Failed to change password", ex)
        }
    }

    override fun deleteUser(userId: UUID) {
        try {
            client
                .users()
                .delete(userId.toString())
        } catch (ex: Exception) {
            throw BadRequestException("Failed to delete user", ex)
        }
    }

    override fun addRealmRoleToUser(userId: UUID, roleName: Role) {
        try {
            val role = client
                .roles()
                .get(roleName.idpRole())
                .toRepresentation()

            val user = client
                .users()
                .get(userId.toString())

            user
                .roles()
                .realmLevel()
                .add(listOf(role))
        } catch (ex: Exception)  {
            throw BadRequestException("Failed to add user '$userId' to role '$roleName'", ex)
        }
    }
}