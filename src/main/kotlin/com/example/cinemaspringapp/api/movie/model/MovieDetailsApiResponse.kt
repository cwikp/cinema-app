package com.example.cinemaspringapp.api.movie.model

import com.example.cinemaspringapp.domain.movie.MovieImdbDetails

data class MovieDetailsApiResponse(
    val movieId: String,
    val name: String,
    val description: String,
    val releaseDate: String,
    val rating: String,
    val imdbRating: String,
    val runtime: String,
)

fun MovieImdbDetails.toApiResponse() = MovieDetailsApiResponse(
    movieId = movieId.value,
    rating = rating.value,
    name = imdbDetails?.name ?: "",
    description = imdbDetails?.description ?: "",
    releaseDate = imdbDetails?.releaseDate ?: "",
    imdbRating = imdbDetails?.rating ?: "",
    runtime = imdbDetails?.runtime ?: ""
)