package com.example.cinemaspringapp.domain.movie.model

data class MovieSingleRating(
    val movieId: MovieId,
    val singleRating: SingleRating
)

data class SingleRating(val value: Int) {
    init {
        require(value in MIN_RATING_VALUE..MAX_RATING_VALUE) {
            "Rating must be between $MIN_RATING_VALUE and $MAX_RATING_VALUE inclusive, got: $value"
        }
    }
}

private const val MIN_RATING_VALUE = 1
private const val MAX_RATING_VALUE = 10
