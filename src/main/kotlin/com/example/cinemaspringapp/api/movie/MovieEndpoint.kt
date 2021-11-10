package com.example.cinemaspringapp.api.movie

import com.example.cinemaspringapp.movie.MovieFacade
import com.example.cinemaspringapp.movie.MovieId
import com.example.cinemaspringapp.movie.MovieRating
import com.example.cinemaspringapp.movie.UserMovieRating
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieEndpoint(private val movieFacade: MovieFacade) {

    @GetMapping("/{movieId}")
    fun getMovieDetails(@PathVariable movieId: String): ResponseEntity<MovieDetailsResponse> =
        movieFacade.movieDetails(MovieId(movieId))
            ?.let { ResponseEntity.ok(it.toResponse()) }
            ?: ResponseEntity.notFound().build()

    @PostMapping("/{movieId}/ratings")
    fun rateMovie(@PathVariable movieId: String, @RequestBody rateMovieRequest: RateMovieRequest) {
        movieFacade.rateMovie(
            UserMovieRating(
                movieId = MovieId(movieId),
                rating = rateMovieRequest.userRating
            )
        ).toResponse(movieId)
    }

    private fun MovieRating.toResponse(movieId: String) = RateMovieResponse(
        movieId = movieId,
        rating = value()
    )

    data class RateMovieRequest(
        val userRating: Long
    )

    data class RateMovieResponse(
        val movieId: String,
        val rating: String
    )
}