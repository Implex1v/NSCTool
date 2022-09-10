package de.nsctool.api.character

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CharacterRepository: CrudRepository<Character, UUID> {
    fun findByName(name: String): List<Character>
    fun findByRace(race: String): List<Character>
    fun findByProfession(profession: String): List<Character>

    @Query("SELECT c FROM Character c WHERE :tag in c.tags")
    fun findByTag(tag: String): List<Character>
}