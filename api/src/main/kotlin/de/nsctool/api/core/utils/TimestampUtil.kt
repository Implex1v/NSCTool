package de.nsctool.api.core.utils

interface TimestampUtil {
    fun nowAsTimestamp(): String
    fun nowAsISOString(): String
}