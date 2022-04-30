package de.nsctool.api.utils

import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class OffsetTimestampUtil: TimestampUtil {
    private val timeZone = ZoneId.of("Europe/Berlin")

    override fun nowAsTimestamp(): String = getNow()
        .toInstant()
        .epochSecond
        .toString()

    override fun nowAsISOString(): String = getNow().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    private fun getNow(): OffsetDateTime = OffsetDateTime.now(timeZone)
}