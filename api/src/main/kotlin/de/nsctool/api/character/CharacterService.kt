package de.nsctool.api.character

import de.nsctool.api.core.exceptions.BadRequestException
import de.nsctool.api.core.exceptions.NotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CharacterService(
    val repository: CharacterRepository
) {
    fun findAll(): Iterable<Character> = repository.findAll()

    fun findById(uuid: UUID): Character {
        return repository
            .findById(uuid)
            .orElseThrow { NotFoundException("character not found") }
    }

    fun deleteById(uuid: UUID) {
        repository.deleteById(uuid)
    }

    fun create(character: Character): Character {
        if (repository.existsById(character.id)) {
            throw BadRequestException("character with id already exists")
        }
        return repository.save(character)
    }
}