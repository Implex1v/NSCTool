package de.nsctool.api.authentication.keycloak

enum class Role(val value: String) {
    USER(Roles.ROLE_USER),
    ADMIN(Roles.ROLE_ADMIN),
    MANAGEMENT(Roles.ROLE_MGMT);

    fun idpRole() = value.replace("ROLE_", "")
}

class Roles {
    companion object {
        const val ROLE_USER = "ROLE_api-user"
        const val ROLE_ADMIN = "ROLE_api-admin"
        const val ROLE_MGMT = "ROLE_api-mgmt"
    }
}