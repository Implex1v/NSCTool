package de.nsctool.api.authentication

import de.nsctool.api.model.User

interface JwtHandler {
    fun parseToken(jwtToken: String): User
    fun generateToken(user: User): String
}