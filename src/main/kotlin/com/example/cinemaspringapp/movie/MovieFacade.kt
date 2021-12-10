package com.example.cinemaspringapp.movie

import com.example.cinemaspringapp.movie.imdb.ImdbMovieDetailsProvider

class MovieFacade(
    private val movieRepository: MovieRepository,
    private val imdbMovieDetailsProvider: ImdbMovieDetailsProvider
) {

    fun createMovie(movie: Movie): MovieDetails =
        movieRepository.createMovie(movie).let { fetchMovieDetails(it) }

    fun movieDetails(movieId: MovieId): MovieDetails? =
        movieRepository.findMovie(movieId)?.let { fetchMovieDetails(it) }

    private fun fetchMovieDetails(movie: Movie): MovieDetails =
        MovieDetails(
            movieId = movie.movieId,
            rating = movie.rating,
            imdbDetails = imdbMovieDetailsProvider.fetchImdbMovieDetails(movie.imdbId)
        )

    fun rateMovie(userMovieRating: UserMovieRating) = movieRepository.storeRating(userMovieRating).rating
}


data class MovieDetails(
    val movieId: MovieId,
    val rating: MovieRating,
    val imdbDetails: ImdbMovieDetails?
)

data class ImdbMovieDetails(
    val name: String,
    val description: String,
    val releaseDate: String,
    val rating: String,
    val runtime: String,
)