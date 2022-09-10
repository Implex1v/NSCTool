package de.nsctool.api.character

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

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
