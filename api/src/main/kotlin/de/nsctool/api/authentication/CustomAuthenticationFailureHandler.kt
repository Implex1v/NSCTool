package de.nsctool.api.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import de.nsctool.api.core.utils.TimestampUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationFailureHandler(
    private val objectMapper: ObjectMapper,
    private val timeUtil: TimestampUtil,
): AuthenticationFailureHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        request ?: logger.warn("onAuthenticationFailure: request is null")
        response ?: logger.warn("onAuthenticationFailure: response is null")
        exception ?: logger.warn("onAuthenticationFailure: exception is null")

        val payload = mutableMapOf<String, String>()
        payload["message"] = exception?.message ?: "Failed to authenticate"
        payload["timestamp"] = timeUtil.nowAsISOString()
        payload["path"] = request?.pathInfo ?: "unknown"

        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.outputStream?.println(objectMapper.writeValueAsString(payload)) ?: logger.error("Response is null")
    }
}


