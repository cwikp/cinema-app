package com.example.cinemaspringapp.api.movie

import com.example.cinemaspringapp.movie.Movie
import com.example.cinemaspringapp.movie.MovieFacade
import com.example.cinemaspringapp.movie.MovieId
import com.example.cinemaspringapp.movie.MovieRating
import com.example.cinemaspringapp.movie.imdb.ImdbIdFactory
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/movies")
class MovieAdminEndpoint(
    private val movieFacade: MovieFacade,
    private val imdbIdFactory: ImdbIdFactory,
) {

    @PostMapping
    fun createMovie(@RequestBody createMovieRequest: CreateMovieRequest): ResponseEntity<MovieDetailsResponse> =
        createMovieRequest.toDomain()
            .let { movieFacade.createMovie(it) }
            .let { ResponseEntity.status(CREATED).body(it.toResponse()) }

    private fun CreateMovieRequest.toDomain() = Movie(
        movieId = MovieId(),
        imdbId = imdbIdFactory.createValidatedImdbId(imdbId),
        rating = MovieRating.initial()
    )

    data class CreateMovieRequest(
        val imdbId: String
    )
}

