package com.example.cinemaspringapp.movie

data class UserMovieRating(
    val movieId: MovieId,
    val rating: Long
) {
    init {
        require(rating in 1..10) { "User rating must be between 1 and 10 inclusive: $rating"}
    }
}