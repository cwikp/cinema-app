package com.example.cinemaspringapp.api.movie

import com.example.cinemaspringapp.BaseIntegrationTest
import com.example.cinemaspringapp.stub.IMDB_RATING
import com.example.cinemaspringapp.stub.OmdbApiStub
import com.example.cinemaspringapp.stub.PLOT
import com.example.cinemaspringapp.stub.RELEASED
import com.example.cinemaspringapp.stub.RUNTIME
import com.example.cinemaspringapp.stub.TITLE
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.http.HttpStatus.OK

@TestInstance(PER_CLASS)
class MovieEndpointTest : BaseIntegrationTest() {

    lateinit var movieRequests: MovieRequests
    lateinit var omdbApiStub: OmdbApiStub

    @BeforeAll
    fun initOnce() {
        movieRequests = MovieRequests(restTemplate)
        omdbApiStub = OmdbApiStub(stubServer)
    }

    @Test
    fun `should get movie details and rate movie`() {
        //given
        omdbApiStub.stubOmdbApiOkResponse(IMDB_ID)
        val movieId = saveMovie()

        //when
        val getResponse = movieRequests.getMovie(movieId)

        //then
        assertTrue(getResponse.statusCode == OK)
        assertEquals(movieDetailsJsonResponse(movieId = movieId, rating = "0.0"), getResponse.body!!)

        //when
        movieRequests.rateMovie(movieId, 7)
        movieRequests.rateMovie(movieId, 10)
        movieRequests.rateMovie(movieId, 6)

        //then
        movieRequests.getMovie(movieId).run {
            assertTrue(statusCode == OK)
            assertEquals(movieDetailsJsonResponse(movieId = movieId, rating = "7.7"), body!!)
        }
    }

    private fun saveMovie(): String = (movieRequests.saveMovie(IMDB_ID).body as Map<*, *>)["movieId"].toString()

    private fun movieDetailsJsonResponse(movieId: String, rating: String) = mapOf(
        "movieId" to movieId,
        "name" to TITLE,
        "description" to PLOT,
        "releaseDate" to RELEASED,
        "rating" to rating,
        "imdbRating" to IMDB_RATING,
        "runtime" to RUNTIME
    )
}

private const val IMDB_ID = "imdb-id-1"