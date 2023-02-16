package de.nsctool.api.user.usercreation

import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.user.User

class UserCreationData(
    var user: User,
    val password: String,
    val roleName: Role = Role.USER,
    var userId: String? = null,
)
