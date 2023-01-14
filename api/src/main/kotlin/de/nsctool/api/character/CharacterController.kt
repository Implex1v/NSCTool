package de.nsctool.api.character

import de.nsctool.api.core.controller.parseUUIDOrThrow
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/characters")
class CharacterController(
    private val service: CharacterService
) {
    @GetMapping()
    fun getAll(): Iterable<Character> = service.findAll()

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: String): Character =
        service.findById(id = uuid) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: String) {
        return service.deleteById(id = uuid)
    }

    @PutMapping("{id}")
    fun upsert(
        @PathVariable id: String,
        @RequestBody character: Character
    ): Character = service.upsert(id, character)
}
