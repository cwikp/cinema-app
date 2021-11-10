package com.example.cinemaspringapp.movie.imdb

class ImdbIdFactory(private val imdbIdValidator: ImdbIdValidator) {

    fun createValidatedImdbId(maybeImdbId: String): ImdbId =
        if (imdbIdValidator.isValidImdbId(maybeImdbId)) {
            ImdbId.validated(maybeImdbId)
        } else throw IllegalArgumentException("Provided id: $maybeImdbId is not a valid imdb id")

    class ImdbId private constructor(val value: String) {
        companion object {
            fun validated(validatedImdbId: String) = ImdbId(validatedImdbId)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ImdbId

            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

        override fun toString(): String {
            return "ImdbId(value='$value')"
        }
    }
}