package de.nsctool.api.character

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import java.util.UUID

@Entity
@Table(name = "characters")
class Character {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
    )
    var id: UUID = UUID.randomUUID()

    var name: String = ""

    var description: String = ""

    var image: ByteArray = byteArrayOf()

    var race: String = ""

    var profession: String = ""

    @ElementCollection
    var tags: List<String> = emptyList()
}
