package de.nsctool.api.authentication

import de.nsctool.api.repository.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter(
    private val userRepository: UserRepository,
    private val jwtTokenUtil: JwtHandler
): OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if(header == null || header.isEmpty() || !header.startsWith("Bearer")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = header.split(" ")[1].trim()

        try {
            val user = jwtTokenUtil.parseToken(token)
            val auth = UsernamePasswordAuthenticationToken(user, null)
            auth.details = WebAuthenticationDetailsSource().buildDetails(request)

            SecurityContextHolder.getContext().authentication = auth
            filterChain.doFilter(request, response)
        } catch (ex: AuthenticationException) {
            filterChain.doFilter(request, response)
        }
    }
}
