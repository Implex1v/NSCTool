package de.nsctool.api.authentication

import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

@Configuration
class KeycloakJwtRoleConverter: Converter<Jwt, AbstractAuthenticationToken?> {

    override fun convert(source: Jwt): AbstractAuthenticationToken {
        val roles = extractResourceRoles(source)
        return JwtAuthenticationToken(source, roles)
    }

    companion object {
        private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
            val resourceAccess: List<Any> = jwt.getClaim("roles")

            if(resourceAccess.isEmpty()) {
                return emptyList()
            }

            return resourceAccess
                .map { SimpleGrantedAuthority(it as String) }
        }
    }
}