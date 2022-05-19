package de.nsctool.api.controller

import de.nsctool.api.repository.UserRepository
import org.springframework.security.access.annotation.Secured
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class HealthController {
    @ResponseBody
    @GetMapping("/health")
    fun health(): Map<String, String> {
        return mapOf("status" to "healthy")
    }

    @ResponseBody
    @GetMapping("/check")
    //@PreAuthorize("hasRole('ROLE_api-admin')")
    @Secured(value = ["ROLE_api-user"])
    fun check(request: HttpServletRequest): Map<String, String> {
        val principal = request.getUser()
        return mapOf("hello" to principal)
    }
}

fun HttpServletRequest.getUser(): String {
    val auth = this.userPrincipal as JwtAuthenticationToken
    return auth.name
}