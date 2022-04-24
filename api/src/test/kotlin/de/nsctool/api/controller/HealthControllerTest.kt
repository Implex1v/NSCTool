package de.nsctool.api.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
internal class HealthControllerTest(
    @Autowired private val mock: MockMvc
) {
    @Test
    fun shouldReturnHealth() {
        mock.perform(get("/health"))
            .andExpect { status().isOk }
            .andExpect { content().contentType(MediaType.APPLICATION_JSON) }
            .andExpect { jsonPath("$.status").value("healthy") }
    }
}