package de.nsctool.api.service

import de.nsctool.api.exceptions.BadRequestException
import de.nsctool.api.exceptions.NotFoundException
import de.nsctool.api.model.Character
import de.nsctool.api.repository.CharacterRepository
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