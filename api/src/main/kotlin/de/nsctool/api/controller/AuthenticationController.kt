package de.nsctool.api.controller

import org.springframework.web.bind.annotation.ResponseBody

//@RestController()
//class AuthenticationController(
//    private val jwtHandler: JwtHandler,
//    private val authenticationManager: AuthenticationManager
//) {
//    @ResponseBody
//    @PostMapping("/login", produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
//        try {
//            val authenticate = authenticationManager
//                .authenticate(
//                    UsernamePasswordAuthenticationToken(request.username, request.password)
//                )
//
//            val user = authenticate.principal as User
//            val jwt = jwtHandler.generateToken(user)
//
//            return ResponseEntity
//                .ok(AuthResponse(jwt))
//        } catch (ex: BadCredentialsException) {
//            throw AuthenticationException("Username or password is invalid", ex)
//        }
//    }
//}

data class AuthRequest(val username: String, val password: String)

@ResponseBody
data class AuthResponse(val jwt: String)
