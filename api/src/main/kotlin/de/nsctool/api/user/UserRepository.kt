package de.nsctool.api.user

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserRepository: CrudRepository<User, UUID> {
    fun findByUserName(username: String): User?
}