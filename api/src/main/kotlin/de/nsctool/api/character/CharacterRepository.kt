package de.nsctool.api.character

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CharacterRepository: MongoRepository<Character, String> {
    fun findByName(name: String): List<Character>
    fun findByRace(race: String): List<Character>
    fun findByProfession(profession: String): List<Character>
    fun findByTagsContains(tag: String): List<Character>
}