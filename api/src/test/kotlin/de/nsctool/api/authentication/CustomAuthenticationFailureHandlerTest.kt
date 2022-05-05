package de.nsctool.api.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import de.nsctool.api.utils.TimestampUtil
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ExtendWith(MockitoExtension::class)
internal class CustomAuthenticationFailureHandlerTest {
     private val objectMapper = mockk<ObjectMapper>()
     private val timestampUtil = mockk<TimestampUtil>()
     private val handler = CustomAuthenticationFailureHandler(objectMapper, timestampUtil)

     @Test
     fun shouldHandleAuthenticationError() {
          val payload = slot<String>()
          val request = mockk<HttpServletRequest>()
          val response = spyk<HttpServletResponse>()
          val exception = mockk<org.springframework.security.core.AuthenticationException>()
          val str = "Bumm"

          every { exception.message } returns "Foo Bar"
          every { timestampUtil.nowAsISOString() } returns "13.07.1993"
          every { request.pathInfo } returns "/test"
          justRun { response.outputStream.println(capture(payload)) }
          every { objectMapper.writeValueAsString(any()) } returns str

          handler.onAuthenticationFailure(request, response, exception)

          verify { response.status = HttpStatus.UNAUTHORIZED.value()  }
          payload.isCaptured shouldBe true
          payload.captured shouldBe str
     }
}