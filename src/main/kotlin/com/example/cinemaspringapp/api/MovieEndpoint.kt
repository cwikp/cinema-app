package com.example.cinemaspringapp.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieEndpoint {

    @GetMapping("/{movieId}")
    fun getMovieDetails(@PathVariable movieId: String): MovieDetails {
        TODO()
    }

    @PostMapping("/{movieId}/ratings")
    fun rateMovie(@PathVariable movieId: String, @RequestBody rateMovieRequest: RateMovieRequest) {
        TODO()
    }

    data class MovieDetails(
        val name: String,
        val description: String,
        val releaseDate: String,
        val rating: String,
        val imdbRating: String,
        val runtime: String,
    )

    data class RateMovieRequest(
        val userId: String,
        val rating: String
    )
}