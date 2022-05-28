package de.nsctool.api.authentication.keycloak

import java.util.UUID

interface KeycloakClient {
    fun createUser(username: String, email: String, password: String, roles: List<Role>): UUID
    fun resetPassword(userId: UUID, password: String, temporary: Boolean)
    fun deleteUser(userId: UUID)
    fun addRealmRoleToUser(userId: UUID, roleName: Role)
}