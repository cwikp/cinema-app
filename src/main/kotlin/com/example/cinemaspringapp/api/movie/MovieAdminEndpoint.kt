package com.example.cinemaspringapp.api.movie

import com.example.cinemaspringapp.api.movie.model.MovieDetailsApiResponse
import com.example.cinemaspringapp.api.movie.model.toApiResponse
import com.example.cinemaspringapp.domain.movie.MovieFacade
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbId
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
) {
    @PostMapping
    fun createMovie(@RequestBody request: CreateMovieApiRequest): ResponseEntity<MovieDetailsApiResponse> =
        movieFacade.createMovie(
            imdbId = ImdbId(request.imdbId)
        ).let { ResponseEntity.status(CREATED).body(it.toApiResponse()) }
}

data class CreateMovieApiRequest(
    val imdbId: String
)