package com.example.cinemaspringapp.domain.movie.model

data class MovieSingleRating(
    val movieId: MovieId,
    val singleRating: SingleRating
)

data class SingleRating(val value: Int) {
    init {
        require(value in 1..10) { "Rating must be between 1 and 10 inclusive: $value" }
    }
}