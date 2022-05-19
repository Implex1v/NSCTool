package de.nsctool.api.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CharacterRepository: CrudRepository<de.nsctool.api.model.Character, UUID> {
    fun findByName(name: String): List<de.nsctool.api.model.Character>
    fun findByRace(race: String): List<de.nsctool.api.model.Character>
    fun findByProfession(profession: String): List<de.nsctool.api.model.Character>

    @Query("SELECT c FROM Character c WHERE :tag in c.tags")
    fun findByTag(tag: String): List<de.nsctool.api.model.Character>
}