package de.nsctool.api.controller

import de.nsctool.api.controller.exceptions.BadRequestException
import java.util.UUID

fun String.parseUUIDOrThrow(): UUID {
    try {
        return UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        throw BadRequestException("uuid is invalid", e)
    }
}