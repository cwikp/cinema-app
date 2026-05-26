package com.example.cinemaspringapp.domain.movie.moviedetails

data class ImdbId(val value: String) {
    init {
        require(value.length in MIN_IMDB_ID_LENGTH..MAX_IMDB_ID_LENGTH) {
            "Valid ImdbId must be between $MIN_IMDB_ID_LENGTH and $MAX_IMDB_ID_LENGTH characters long, got: $value"
        }
        require(value.take(IMDB_ID_PREFIX_LENGTH).onlyLetters()) {
            "Valid ImdbId must contain prefix with $IMDB_ID_PREFIX_LENGTH letters, got: $value"
        }
    }
}

private fun String.onlyLetters() = all { it.isLetter() }
private const val MIN_IMDB_ID_LENGTH = 5
private const val MAX_IMDB_ID_LENGTH = 10
private const val IMDB_ID_PREFIX_LENGTH = 2

