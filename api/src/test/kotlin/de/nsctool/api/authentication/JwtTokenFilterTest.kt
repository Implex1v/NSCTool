package de.nsctool.api.authentication

import de.nsctool.api.model.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

internal class JwtTokenFilterTest {
    private val handler = mockk<JwtHandler>()
    private val filter = JwtTokenFilterStub(handler)

    @Test
    fun testDoFilterInternal() {
        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val filterChain = spyk<FilterChain>()
        val user = mockk<User>()
        val session = mockk<HttpSession>()
        every { request.getHeader(HttpHeaders.AUTHORIZATION) } returns "Bearer Foo"
        every { handler.parseToken("Foo") } returns user
        every { request.getAttribute(any()) } returns null
        every { request.remoteAddr } returns "127.0.0.1"
        every { request.getSession(false) } returns session
        every { session.id } returns "id"

        filter.doFilterInternal(request, response, filterChain)

        verify { filterChain.doFilter(request, response) }
    }

    @Test
    fun testDoFilterInternalError() {
        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val filterChain = spyk<FilterChain>()
        every { request.getHeader(HttpHeaders.AUTHORIZATION) } returns "Bearer Foo"
        every { handler.parseToken("Foo") } throws AuthenticationException("Foo", null)

        filter.doFilterInternal(request, response, filterChain)

        verify { filterChain.doFilter(request, response) }
    }

    private class JwtTokenFilterStub(jwtHandler: JwtHandler): JwtTokenFilter(jwtHandler) {
        public override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
        ) {
            super.doFilterInternal(request, response, filterChain)
        }
    }
}