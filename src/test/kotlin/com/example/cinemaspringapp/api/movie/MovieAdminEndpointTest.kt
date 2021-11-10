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
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

@TestInstance(PER_CLASS)
class MovieAdminEndpointTest : BaseIntegrationTest() {

    lateinit var movieRequests: MovieRequests
    lateinit var omdbApiStub: OmdbApiStub

    @BeforeAll
    fun initOnce() {
        movieRequests = MovieRequests(restTemplate)
        omdbApiStub = OmdbApiStub(stubServer)
    }

    @Test
    fun `should save new movie`() {
        //given
        omdbApiStub.stubOmdbApiOkResponse(IMDB_ID)

        //when
        val saveResponse = movieRequests.saveMovie(IMDB_ID)

        //then
        assertTrue(saveResponse.statusCode == CREATED)

        //and
        movieRequests.getMovie(saveResponse.movieId()).run {
            assertTrue(statusCode == OK)
            assertEquals(
                movieDetailsJsonResponse(saveResponse.movieId()),
                body!!
            )
        }
    }

    @Test
    fun `should throw bad request error if provided id is not a valid imdbId`() {
        //given
        omdbApiStub.stubOmdbApiErrorResponse(NON_EXISTENT_IMDB_ID)

        //when
        val saveResponse = movieRequests.saveMovie(NON_EXISTENT_IMDB_ID)

        //then
        assertTrue(saveResponse.statusCode == BAD_REQUEST)
    }

    @Test
    fun `should throw server error if request to omdb api fails`() {
        //given
        omdbApiStub.stubOmdbApi500Response(IMDB_ID)

        //when
        val saveResponse = movieRequests.saveMovie(IMDB_ID)

        //then
        assertTrue(saveResponse.statusCode == INTERNAL_SERVER_ERROR)
    }

    private fun movieDetailsJsonResponse(movieId: String) = mapOf(
        "movieId" to movieId,
        "name" to TITLE,
        "description" to PLOT,
        "releaseDate" to RELEASED,
        "rating" to "0.0",
        "imdbRating" to IMDB_RATING,
        "runtime" to RUNTIME
    )

    private fun ResponseEntity<Any>.movieId(): String = (this.body as Map<*, *>)["movieId"].toString()
}

private const val IMDB_ID = "imdb-id-1"
private const val NON_EXISTENT_IMDB_ID = "non-existent-id"
