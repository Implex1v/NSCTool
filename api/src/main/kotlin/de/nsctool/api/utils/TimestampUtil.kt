package de.nsctool.api.utils

interface TimestampUtil {
    fun nowAsTimestamp(): String
    fun nowAsISOString(): String
}