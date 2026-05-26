package com.example.cinemaspringapp.domain.movie.repository

import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbId
import com.example.cinemaspringapp.domain.movie.model.MovieId
import com.example.cinemaspringapp.domain.movie.model.MovieRating
import com.example.cinemaspringapp.domain.movie.model.MovieSingleRating

interface MovieRepository {
    fun storeRating(movieSingleRating: MovieSingleRating): Movie
    fun findMovie(movieId: MovieId): Movie?
    fun createMovie(movie: Movie): Movie
}

data class Movie(
    val movieId: MovieId,
    val imdbId: ImdbId,
    val rating: MovieRating
)

