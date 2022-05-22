package de.nsctool.api.authentication.keycloak

enum class Role(val value: String) {
    USER(Roles.ROLE_USER),
    ADMIN(Roles.ROLE_ADMIN),
    MANAGEMENT(Roles.ROLE_MGMT);
}

class Roles {
    companion object {
        const val ROLE_USER = "ROLE-api-user"
        const val ROLE_ADMIN = "ROLE-api-admin"
        const val ROLE_MGMT = "ROLE-api-mgmt"
    }
}