package de.nsctool.api.authentication.keycloak

interface KeycloakClient {
    fun createUser(username: String, email: String, roles: List<Role>): String
    fun resetPassword(userId: String, password: String, temporary: Boolean)
    fun deleteUser(userId: String)
    fun addRealmRoleToUser(userId: String, roleName: Role)
}