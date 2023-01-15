package de.nsctool.api.authentication.keycloak

import de.nsctool.api.core.exceptions.BadRequestException
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.ws.rs.ClientErrorException
import javax.ws.rs.WebApplicationException

@Component
class DefaultKeycloakClient(
    private val meterRegistry: MeterRegistry,
): KeycloakClient {
    private val logger by lazy { LoggerFactory.getLogger(DefaultKeycloakClient::class.java) }

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
        roles: List<Role>,
    ): String = try {
        val userResource = client.users()
        val user = UserRepresentation().apply {
            isEnabled = true
            setUsername(username)
            setEmail(email)
            realmRoles = roles.map { it.idpRole() }
        }

        meterRegistry.clientRequestSummary("createUser") {
            userResource.create(user).let {
                CreatedResponseUtil.getCreatedId(it)
            }
        }
    } catch (ex: Exception) {
        // sadly we don't know what exception is thrown on error => diaper pattern
        throw BadRequestException("Failed to register user", ex)
    }

    override fun resetPassword(userId: String, password: String, temporary: Boolean) {
        try {
            val passwordReset = CredentialRepresentation().apply {
                isTemporary = temporary
                type = CredentialRepresentation.PASSWORD
                value = password
            }
            val user = getUser(userId)

            meterRegistry.clientRequestSummary("resetPassword") {
                user.resetPassword(passwordReset)
            }
        } catch (ex: Exception) {
            throw BadRequestException("Failed to change password", ex)
        }
    }

    fun getUser(userId: String): UserResource = meterRegistry.clientRequestSummary("getUser") {
        client
            .users()
            .get(userId)
    }

    override fun deleteUser(userId: String) {
        try {
            meterRegistry.clientRequestSummary("deleteUser") {
                client
                    .users()
                    .delete(userId)
            }
        } catch (ex: Exception) {
            throw BadRequestException("Failed to delete user", ex)
        }
    }

    fun getRole(role: Role): RoleRepresentation = meterRegistry.clientRequestSummary("getRole") {
        client
            .roles()
            .get(role.idpRole())
            .toRepresentation()
    }

    override fun addRealmRoleToUser(userId: String, role: Role) {
        try {
            val role = getRole(role)
            val user = getUser(userId)

            meterRegistry.clientRequestSummary("addRealmRoleToUser") {
                user
                    .roles()
                    .realmLevel()
                    .add(listOf(role))
            }
        } catch (ex: ClientErrorException)  {
            logger.error("Failed to add user '$userId' to role '${role.idpRole()}': '${ex.response.readEntity(String::class.java)}'")
            throw BadRequestException("Failed to add user '$userId' to role '$role'", ex)
        }
    }

    private fun <T> MeterRegistry.clientRequestSummary(
        name: String,
        block: () -> T,
    ): T {
        val before = Instant.now()
        return try {
            val result = block()
            val duration = Duration.between(before, Instant.now())
            recordTimer(name, "true", "2XX", duration)
            result
        } catch (ex: Exception) {
            val status = when(ex) {
                is WebApplicationException -> {
                    ex.response.status.toString()
                }
                else -> "XXX"
            }

            val duration = Duration.between(before,  Instant.now())
            recordTimer(name, "false", status, duration)
            throw ex
        }
    }

    private fun MeterRegistry.recordTimer(
        name: String,
        success: String,
        status: String,
        duration: Duration
    ) = Timer.builder("http_client_requests_seconds")
        .tag("host", baseUri)
        .tag("name", name)
        .tag("status", status)
        .tag("success", success)
        .publishPercentiles(0.25, 0.5, 0.75, 0.9, 0.99)
        .register(this)
        .record(duration)
}