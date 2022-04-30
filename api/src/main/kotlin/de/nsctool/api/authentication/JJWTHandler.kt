package de.nsctool.api.authentication

import de.nsctool.api.model.User
import de.nsctool.api.repository.UserRepository
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import java.util.UUID

// TODO: Make key configurable
@Component
class JJWTHandler(
    private val userRepository: UserRepository,
): JwtHandler {
    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val jwtParser = Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()

    override fun parseToken(jwtToken: String): User {
        try {
            val claims = jwtParser.parseClaimsJws(jwtToken)
            val userId = UUID.fromString(claims.body.subject)
            return userRepository
                .findById(userId)
                .get()
        } catch (ex: Exception) {
            when(ex::class.java) {
                JwtException::class.java -> System.err.println("Failed to parse JWT token: '${ex.message}'")
                NoSuchElementException::class.java -> System.err.println("User in JWT not found")
                ClassCastException::class.java -> System.err.println("Failed to extract claim from JWT")
            }

            throw AuthenticationException("JWT token is invalid", ex)
        }
    }

    override fun generateToken(user: User): String {
        val claims = Jwts.claims()
            .setSubject(user.id.toString())
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(Date())

        return Jwts
            .builder()
            .setClaims(claims)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}