package de.nsctool.api.controller

import de.nsctool.api.authentication.AuthenticationException
import de.nsctool.api.authentication.JwtHandler
import de.nsctool.api.model.User
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController()
class AuthenticationController(
    private val jwtHandler: JwtHandler,
    private val authenticationManager: AuthenticationManager
) {
    @ResponseBody
    @PostMapping("/login", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        try {
            val authenticate = authenticationManager
                .authenticate(
                    UsernamePasswordAuthenticationToken(request.username, request.password)
                )

            val user = authenticate.principal as User
            val jwt = jwtHandler.generateToken(user)

            return ResponseEntity
                .ok(AuthResponse(jwt))
        } catch (ex: BadCredentialsException) {
            throw AuthenticationException("Username or password is invalid", ex)
        }
    }
}

data class AuthRequest(val username: String, val password: String)

@ResponseBody
data class AuthResponse(val jwt: String)
