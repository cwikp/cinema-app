package com.example.cinemaspringapp.api.movie

import com.example.cinemaspringapp.movie.MovieDetails

data class MovieDetailsResponse(
    val movieId: String,
    val name: String,
    val description: String,
    val releaseDate: String,
    val rating: String,
    val imdbRating: String,
    val runtime: String,
)

fun MovieDetails.toResponse() = MovieDetailsResponse(
    movieId = movieId.value,
    rating = rating.value(),
    name = imdbDetails?.name ?: "",
    description = imdbDetails?.description ?: "",
    releaseDate = imdbDetails?.releaseDate ?: "",
    imdbRating = imdbDetails?.rating ?: "",
    runtime = imdbDetails?.runtime ?: ""
)