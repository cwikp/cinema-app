package com.example.cinemaspringapp.domain.show.model

@JvmInline
value class ShowName private constructor(val value: String) {
    init {
        require(value.length in 5..30) { "ShowName must be between 5 and 30 characters long: $value" }
    }

    companion object {
        fun fromString(value: String): ShowName {
            val normalizedValue = value.trim()
            return ShowName(normalizedValue)
        }
    }
}