package de.nsctool.api.controller

import de.nsctool.api.controller.exceptions.BadRequestException
import de.nsctool.api.controller.exceptions.NotFoundException
import de.nsctool.api.repository.CharacterRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

class CharacterControllerTest (
) {
    private val repository = spyk<CharacterRepository>()
    private val controller = CharacterController(repository)
    private val chars = listOf(
        de.nsctool.api.model.Character(), de.nsctool.api.model.Character()
    )
    private val uuid = UUID.randomUUID()

    @Test
    fun `should get all Characters`() {
        every { repository.findAll() } returns emptyList()
        controller.getAll() shouldHaveSize 0
        verify { repository.findAll() }

        every { repository.findAll() } returns chars
        controller.getAll() shouldBe chars
        verify { repository.findAll() }
    }

    @Test
    fun `should get a character by it's id`() {
        shouldThrowExactly<BadRequestException> { controller.getById("NotAUUID") }

        every { repository.findById(uuid) } returns Optional.of(chars[0])
        controller.getById(uuid.toString()) shouldBe chars[0]

        every { repository.findById(uuid) } returns Optional.empty()
        shouldThrowExactly<NotFoundException> { controller.getById(uuid.toString()) }
    }

    @Test
    fun `should delete character by id`() {
        shouldThrowExactly<BadRequestException> { controller.delete("NotAUUID") }

        justRun { repository.deleteById(uuid) }
        controller.delete(uuid.toString())
        verify { repository.deleteById(uuid) }

        every { repository.deleteById(uuid) }.throws(IllegalArgumentException())
        shouldThrowExactly<NotFoundException> { controller.delete(uuid.toString()) }
    }

    @Test
    fun `should add character`() {
        every { repository.existsById(chars[0].id) } returns false
        every { repository.save(chars[0]) } returns chars[0]
        controller.create(chars[0]) shouldBe chars[0]
    }

    @Test
    fun `should not add character if exists`() {
        every { repository.existsById(chars[0].id) } returns true
        shouldThrowExactly<BadRequestException> { controller.create(chars[0]) }
        verify(exactly = 0) { repository.save(any()) }
    }
}