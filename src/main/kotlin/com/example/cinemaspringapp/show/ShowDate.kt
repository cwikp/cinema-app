package com.example.cinemaspringapp.show

import java.time.LocalDateTime
import java.time.ZoneId

data class ShowDate(
    val localDateTime: LocalDateTime,
    val zoneId: ZoneId
) {
    fun zonedDate(): String = localDateTime.atZone(zoneId).toString()
}