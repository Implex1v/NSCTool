package de.nsctool.api.character

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document
data class Character (
    @Id
    var id: String = UUID.randomUUID().toString(),
    @Indexed
    var name: String = "",
    var description: String = "",
    var race: String = "",
    var profession: String = "",
    @Indexed
    var tags: List<String> = emptyList(),
) {
    var image: ByteArray = byteArrayOf()
}
