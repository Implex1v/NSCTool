package de.nsctool.api.controller

import de.nsctool.api.model.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
    fun check(request: HttpServletRequest): Map<String, String> {
        val principal = request.getUser()
        return mapOf("hello" to principal.userName)
    }
}

fun HttpServletRequest.getUser(): User {
    val auth = this.userPrincipal as UsernamePasswordAuthenticationToken
    return auth.principal as User
}