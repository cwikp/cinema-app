package com.example.cinemaspringapp.domain.movie.model

import java.util.*

data class MovieId(private val uuid: UUID = UUID.randomUUID()) {
    val value = uuid.toString()

    companion object {
        fun fromString(value: String): MovieId =
            MovieId(UUID.fromString(value))
    }
}