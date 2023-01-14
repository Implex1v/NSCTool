package de.nsctool.api.character

import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CharacterService(
    val repository: CharacterRepository
) {
    fun findAll(): Iterable<Character> = repository.findAll()

    fun findById(id: String): Character? =
        repository
            .findById(id)
            .getOrNull()

    fun deleteById(id: String) = repository.deleteById(id)

    fun upsert(id: String, character: Character): Character = repository.save(character.copy(id = id))
}