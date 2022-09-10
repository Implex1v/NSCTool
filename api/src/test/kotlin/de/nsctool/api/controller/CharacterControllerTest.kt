package de.nsctool.api.controller

import de.nsctool.api.exceptions.BadRequestException
import de.nsctool.api.exceptions.NotFoundException
import de.nsctool.api.service.CharacterService
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.Test
import java.util.UUID

class CharacterControllerTest (
) {
    private val service = mockk<CharacterService>()
    private val controller = CharacterController(service)
    private val chars = listOf(
        de.nsctool.api.model.Character(), de.nsctool.api.model.Character()
    )
    private val uuid = UUID.randomUUID()

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
        shouldThrowExactly<BadRequestException> { controller.getById("NotAUUID") }

        every { service.findById(uuid) } returns chars[0]
        controller.getById(uuid.toString()) shouldBe chars[0]

        every { service.findById(uuid) } throws(NotFoundException("Foo"))
        shouldThrowExactly<NotFoundException> { controller.getById(uuid.toString()) }
    }

    @Test
    fun `should delete character by id`() {
        shouldThrowExactly<BadRequestException> { controller.delete("NotAUUID") }

        justRun { service.deleteById(uuid) }
        controller.delete(uuid.toString())
        verify { service.deleteById(uuid) }
    }

    @Test
    fun `should add character`() {
        every { service.create(chars[0]) } returns chars[0]
        controller.create(chars[0]) shouldBe chars[0]
    }
}