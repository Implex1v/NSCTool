package de.nsctool.api.controller

import de.nsctool.api.controller.exceptions.BadRequestException
import de.nsctool.api.controller.exceptions.NotFoundException
import de.nsctool.api.model.Character
import de.nsctool.api.repository.CharacterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/characters")
class CharacterController(
    @Autowired
    private val repository: CharacterRepository
) {

    @ResponseBody
    @GetMapping()
    fun getAll(): Iterable<Character> = repository.findAll()

    @ResponseBody
    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: String): Character {
        val mappedUUID = uuid.parseUUIDOrThrow()
        return repository
            .findById(mappedUUID)
            .orElseThrow { NotFoundException("character not found") }
    }

    @ResponseBody
    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: String) {
        val mappedUUID = uuid.parseUUIDOrThrow()

        try {
            repository.deleteById(mappedUUID)
        } catch (e: IllegalArgumentException) {
            throw NotFoundException("character not found")
        }
    }

    @ResponseBody
    @PostMapping
    fun create(@RequestBody character: Character): Character {
        if (repository.existsById(character.id)) {
            throw BadRequestException("character with id already exists")
        }
        return repository.save(character)
    }

}
