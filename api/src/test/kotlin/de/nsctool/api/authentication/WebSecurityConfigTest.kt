package de.nsctool.api.authentication

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
internal class WebSecurityConfigTest(
    @Autowired private val mock: MockMvc
) {
    @Test
    fun shouldOnlyAllowHealthForUnauthenticated() {
        mock.perform(get("/health"))
            .andExpect { status().isOk }

        mock.perform(get("/"))
            .andExpect { status().isUnauthorized }
    }
}