package com.example.cinemaspringapp.domain.show.model

import java.util.UUID

data class ShowId(private val uuid: UUID = UUID.randomUUID()) {
    val value = uuid.toString()

    companion object {
        fun fromString(value: String): ShowId =
            ShowId(UUID.fromString(value))
    }
}