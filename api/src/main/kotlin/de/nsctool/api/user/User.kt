package de.nsctool.api.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @Id
    var id: String,
    @Indexed
    var userName: String,
    @Indexed
    var email: String,
    var characters: List<String> = emptyList(),
)