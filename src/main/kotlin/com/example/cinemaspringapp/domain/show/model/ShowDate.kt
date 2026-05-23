package com.example.cinemaspringapp.domain.show.model

import java.time.LocalDateTime
import java.time.ZoneId

data class ShowDate(
    val localDateTime: LocalDateTime,
    val zoneId: ZoneId
) {
    fun zonedDate(): String = localDateTime.atZone(zoneId).toString()
}