package de.nsctool.api.character

import de.nsctool.api.core.controller.parseUUIDOrThrow
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/characters")
class CharacterController(
    private val service: CharacterService
) {
    @ResponseBody
    @GetMapping()
    fun getAll(): Iterable<Character> = service.findAll()

    @ResponseBody
    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: String): Character {
        val mappedUUID = uuid.parseUUIDOrThrow()
        return service.findById(uuid = mappedUUID)
    }

    @ResponseBody
    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: String) {
        val mappedUUID = uuid.parseUUIDOrThrow()
        return service.deleteById(uuid = mappedUUID)
    }

    @ResponseBody
    @PostMapping
    fun create(@RequestBody character: Character): Character = service.create(character = character)
}
