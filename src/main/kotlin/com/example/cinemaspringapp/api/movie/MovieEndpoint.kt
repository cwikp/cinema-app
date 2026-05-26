package com.example.cinemaspringapp.api.movie

import com.example.cinemaspringapp.api.movie.model.MovieDetailsApiResponse
import com.example.cinemaspringapp.api.movie.model.toApiResponse
import com.example.cinemaspringapp.domain.movie.MovieFacade
import com.example.cinemaspringapp.domain.movie.model.MovieId
import com.example.cinemaspringapp.domain.movie.model.MovieRating
import com.example.cinemaspringapp.domain.movie.model.MovieSingleRating
import com.example.cinemaspringapp.domain.movie.model.SingleRating
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
    fun getMovieDetails(@PathVariable movieId: String): ResponseEntity<MovieDetailsApiResponse> {
        val movieDetails = movieFacade.movieDetails(MovieId.fromString(movieId))
        return movieDetails
            ?.let { ResponseEntity.ok(it.toApiResponse()) }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping("/{movieId}/ratings")
    fun rateMovie(@PathVariable movieId: String, @RequestBody rateMovieRequest: RateMovieRequest): ResponseEntity<RateMovieResponse> {
        val movieRating = movieFacade.rateMovie(
            rateMovieRequest.toDomain(movieId)
        )
        return ResponseEntity.ok(movieRating.toApiResponse(movieId))
    }
}

private fun RateMovieRequest.toDomain(
    movieId: String,
): MovieSingleRating = MovieSingleRating(
    movieId = MovieId.fromString(movieId),
    singleRating = SingleRating(this.userRating)
)

private fun MovieRating.toApiResponse(movieId: String) = RateMovieResponse(
    movieId = movieId,
    rating = this.value
)

data class RateMovieRequest(
    val userRating: Int
)

data class RateMovieResponse(
    val movieId: String,
    val rating: String
)