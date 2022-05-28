package de.nsctool.api.controller

import org.springframework.security.access.annotation.Secured
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
}
