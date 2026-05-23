package com.example.cinemaspringapp.domain.movie.moviedetails

data class ImdbId(val value: String) {
    init {
        require(value.length in 2..10) { "Valid ImdbId must be between 2 and 10 characters long: $value" }
        require(value.take(2).onlyLetters()) { "Valid ImdbId must contain prefix with 2 letters: $value" }
    }
}

private fun String.onlyLetters() = all { it.isLetter() }
