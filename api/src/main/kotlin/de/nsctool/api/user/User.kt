package de.nsctool.api.user

import de.nsctool.api.character.Character
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    var id: UUID = UUID.randomUUID(),
    @Column(unique = true)
    var userName: String,
    @Column(unique = true)
    var email: String,
    @OneToMany
    var characters: List<Character> = emptyList(),
)