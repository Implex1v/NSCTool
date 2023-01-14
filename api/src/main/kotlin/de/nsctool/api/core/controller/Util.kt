package de.nsctool.api.core.controller

import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.core.exceptions.BadRequestException
import de.nsctool.api.core.exceptions.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID

fun String.parseUUIDOrThrow(): UUID {
    try {
        return UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        throw BadRequestException("uuid is invalid", e)
    }
}

fun HttpServletRequest.hasUserRole(role: Role): Boolean {
    return try {
        this.getAuth()
            .authorities
            .any { it.authority == role.value }
    } catch (ex: Exception) {
        false
    }
}

fun HttpServletRequest.getUsername(): String {
    val auth = this.getAuth()
    return auth.name ?: throw UnauthorizedException("User is not authenticated")
}

fun HttpServletRequest.isUser(userId: String) = this.getAuth().name == userId


private fun HttpServletRequest.getAuth(): JwtAuthenticationToken {
    return try {
        this.userPrincipal as JwtAuthenticationToken
    } catch (ex: TypeCastException) {
        throw UnauthorizedException("Authentication is required")
    }
}
