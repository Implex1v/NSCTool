package de.nsctool.api.repository

import de.nsctool.api.model.User
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserRepository: CrudRepository<User, UUID> {
    fun findByUserName(username: String): User?
}