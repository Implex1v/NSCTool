package de.nsctool.api.character

import de.nsctool.api.core.exceptions.BadRequestException
import de.nsctool.api.core.exceptions.NotFoundException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*

class CharacterControllerTest (
) {
    private val service = mockk<CharacterService>()
    private val controller = CharacterController(service)
    private val chars = listOf(
        Character(), Character()
    )
    private val uuid = UUID.randomUUID().toString()

    @Test
    fun `should get all Characters`() {
        every { service.findAll() } returns emptyList()
        controller.getAll() shouldHaveSize 0
        verify { service.findAll() }

        every { service.findAll() } returns chars
        controller.getAll() shouldBe chars
        verify { service.findAll() }
    }

    @Test
    fun `should get a character by it's id`() {
        every { service.findById(uuid) } returns chars[0]
        controller.getById(uuid) shouldBe chars[0]

        every { service.findById(uuid) } throws(NotFoundException("Foo"))
        shouldThrowExactly<NotFoundException> { controller.getById(uuid) }
    }

    @Test
    fun `should delete character by id`() {
        justRun { service.deleteById(uuid) }
        controller.delete(uuid)
        verify { service.deleteById(uuid) }
    }

    @Test
    fun `should add character`() {
        every { service.upsert("", chars[0]) } returns chars[0]
        controller.upsert("", chars[0]) shouldBe chars[0]
    }
}