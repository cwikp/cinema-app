package com.example.cinemaspringapp.api.movie

import com.example.cinemaspringapp.api.movie.MovieEndpoint.RateMovieResponse
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity

class MovieRequests(private val restTemplate: TestRestTemplate) {

    fun saveMovie(imdbId: String): ResponseEntity<Any> =
        restTemplate.exchange(
            "/api/admin/movies",
            POST,
            HttpEntity(saveMovieRequestJson(imdbId), headers()),
            Any::class.java
        )

    fun getMovie(movieId: String): ResponseEntity<Any> =
        restTemplate.exchange(
            "/api/movies/${movieId}",
            GET,
            HttpEntity(null, headers()),
            Any::class.java
        )

    fun rateMovie(movieId: String, userRating: Long): ResponseEntity<RateMovieResponse> =
        restTemplate.exchange(
            "/api/movies/${movieId}/ratings",
            POST,
            HttpEntity(userRatingRequestJson(userRating), headers()),
            RateMovieResponse::class.java
        )


    private fun headers() = HttpHeaders().apply {
        contentType = APPLICATION_JSON
        accept = listOf(APPLICATION_JSON)
    }

    private fun saveMovieRequestJson(imdbId: String) = """
        {
            "imdbId": "$imdbId"
        }
    """

    private fun userRatingRequestJson(userRating: Long) = """
        {
            "userRating": $userRating
        }
    """
}