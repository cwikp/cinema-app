package com.example.cinemaspringapp.movie

import com.example.cinemaspringapp.movie.imdb.ImdbIdFactory.ImdbId

interface MovieRepository {
    fun storeRating(userMovieRating: UserMovieRating): Movie
    fun findMovie(movieId: MovieId): Movie?
    fun createMovie(movie: Movie): Movie
}

data class Movie(
    val movieId: MovieId,
    val imdbId: ImdbId,
    val rating: MovieRating
)

data class UserMovieRating(
    val movieId: MovieId,
    val rating: Long
)