package de.nsctool.api.authentication.keycloak

import java.util.UUID

interface KeycloakClient {
    fun createUser(username: String, email: String, password: String, roles: List<Role>): String
    fun resetPassword(userId: String, password: String, temporary: Boolean)
    fun deleteUser(userId: String)
    fun addRealmRoleToUser(userId: String, roleName: Role)
}