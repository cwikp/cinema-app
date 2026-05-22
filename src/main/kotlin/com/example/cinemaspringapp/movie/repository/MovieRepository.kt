package com.example.cinemaspringapp.movie.repository

import com.example.cinemaspringapp.movie.client.imdb.ImdbIdFactory.ImdbId
import com.example.cinemaspringapp.movie.model.MovieId
import com.example.cinemaspringapp.movie.model.MovieRating
import com.example.cinemaspringapp.movie.model.UserMovieRating

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

