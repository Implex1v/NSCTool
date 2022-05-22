package de.nsctool.api.authentication.keycloak

interface KeycloakClient {
    fun createUser(username: String, email: String, password: String, roles: List<Role>): String
    fun resetPassword(userId: String, password: String, temporary: Boolean)
}