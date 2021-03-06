package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.api.show.ShowEndpoint.ShowsResponse
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity

class ShowRequests(private val restTemplate: TestRestTemplate) {

    fun saveShow(
        name: String = SHOW_NAME,
        movieId: String = MOVIE_ID,
        date: String = MOVIE_LOCAL_DATE_TIME,
        price: String = MOVIE_BASE_PRICE
    ): ResponseEntity<ShowResponse> =
        restTemplate.exchange(
            "/api/admin/shows",
            POST,
            HttpEntity(saveShowRequestJson(name, movieId, date, price), headers()),
            ShowResponse::class.java
        )

    fun updateShow(
        showId: String,
        name: String = SHOW_NAME,
        movieId: String = MOVIE_ID,
        date: String = MOVIE_LOCAL_DATE_TIME,
        price: String = MOVIE_BASE_PRICE
    ): ResponseEntity<ShowResponse> =
        restTemplate.exchange(
            "/api/admin/shows/$showId",
            PUT,
            HttpEntity(updateShowRequestJson(name, movieId, date, price), headers()),
            ShowResponse::class.java
        )

    fun getShows(): ResponseEntity<ShowsResponse> =
        restTemplate.exchange(
            "/api/shows",
            GET,
            HttpEntity(null, headers()),
            ShowsResponse::class.java
        )

    private fun headers() = HttpHeaders().apply {
        contentType = APPLICATION_JSON
        accept = listOf(APPLICATION_JSON)
    }

    private fun saveShowRequestJson(name: String, movieId: String, date: String, price: String) = """
        {
            "name": "$name",
            "movieId": "$movieId",
            "date": {
                "localDateTime": "$date"
            },
            "price": {
                "basePrice": "$price"
            }
        }
    """

    private fun updateShowRequestJson(name: String, movieId: String, date: String, price: String) = """
        {
            "name": "$name",
            "movieId": "$movieId",
            "date": {
                "localDateTime": "$date"
            },
            "price": {
                "basePrice": "$price"
            }
        }
    """
}

const val SHOW_NAME = "show-name-1"
const val MOVIE_ID = "movie-id-1"
const val MOVIE_LOCAL_DATE_TIME = "2021-11-08T00:00"
const val ZONE = "+01:00[Europe/Warsaw]"
const val MOVIE_BASE_PRICE = "10.00"
