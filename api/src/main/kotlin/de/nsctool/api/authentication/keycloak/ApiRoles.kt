package de.nsctool.api.authentication.keycloak

enum class ApiRoles(val value: String) {
    USER("ROLE-api-user"),
    ADMIN("ROLE-api-admin"),
}