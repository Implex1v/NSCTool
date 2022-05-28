package de.nsctool.api.model

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "users")
class User {
    @Id
    var id: UUID = UUID.randomUUID()

    @Column(unique = true)
    var userName: String = ""

    @Column(unique = true)
    var email: String = ""

    @OneToMany()
    var characters: List<Character> = emptyList()
}