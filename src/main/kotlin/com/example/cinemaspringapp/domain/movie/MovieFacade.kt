package com.example.cinemaspringapp.domain.movie

import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbMovieDetailsProvider
import com.example.cinemaspringapp.domain.movie.exception.ImdbMovieNotFoundException
import com.example.cinemaspringapp.domain.movie.exception.MovieNotFoundException
import com.example.cinemaspringapp.domain.movie.model.MovieId
import com.example.cinemaspringapp.domain.movie.model.MovieRating
import com.example.cinemaspringapp.domain.movie.model.MovieSingleRating
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbId
import com.example.cinemaspringapp.domain.movie.repository.Movie
import com.example.cinemaspringapp.domain.movie.repository.MovieRepository
import io.github.oshai.kotlinlogging.KotlinLogging

class MovieFacade(
    private val movieRepository: MovieRepository,
    private val imdbMovieDetailsProvider: ImdbMovieDetailsProvider
) {

    private val logger = KotlinLogging.logger {}

    fun createMovie(imdbId: ImdbId): MovieImdbDetails {
        logger.info { "Creating movie for imdbID: $imdbId" }
        val imdbMovieDetails = imdbMovieDetailsProvider.fetchImdbMovieDetails(imdbId)
            ?: throw ImdbMovieNotFoundException(imdbId.value)

        val movie = newMovieEntry(imdbId)
        return movieRepository.createMovie(movie)
            .toMovieImdbDetails(imdbMovieDetails)
    }

    fun movieDetails(movieId: MovieId): MovieImdbDetails? =
        findMovie(movieId)?.let {
            val imdbMovieDetails = imdbMovieDetailsProvider.fetchImdbMovieDetails(it.imdbId)
            it.toMovieImdbDetails(imdbMovieDetails)
        }

    fun findMovie(movieId: MovieId): Movie? {
        return movieRepository.findMovie(movieId)
    }

    fun rateMovie(movieSingleRating: MovieSingleRating): MovieRating {
        val movieId = movieSingleRating.movieId
        findMovie(movieId) ?: throw MovieNotFoundException(movieId)

        return movieRepository.storeRating(movieSingleRating).rating
    }

    private fun newMovieEntry(imdbId: ImdbId): Movie =
        Movie(
            movieId = MovieId(),
            rating = MovieRating.INITIAL,
            imdbId = imdbId
        )

    private fun Movie.toMovieImdbDetails(imdbDetails: ImdbDetails?): MovieImdbDetails =
        MovieImdbDetails(
            movieId = this.movieId,
            rating = this.rating,
            imdbDetails = imdbDetails
        )
}


data class MovieImdbDetails(
    val movieId: MovieId,
    val rating: MovieRating,
    val imdbDetails: ImdbDetails?
)

data class ImdbDetails(
    val name: String,
    val description: String,
    val releaseDate: String,
    val rating: String,
    val runtime: String,
)