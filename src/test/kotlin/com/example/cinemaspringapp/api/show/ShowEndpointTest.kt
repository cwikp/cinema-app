package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.BaseIntegrationTest
import com.example.cinemaspringapp.api.movie.MovieRequests
import com.example.cinemaspringapp.stub.OmdbApiStub
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

@TestInstance(PER_CLASS)
class ShowEndpointTest : BaseIntegrationTest() {

    lateinit var showRequests: ShowRequests
    lateinit var movieRequests: MovieRequests
    lateinit var omdbApiStub: OmdbApiStub

    @BeforeAll
    fun initOnce() {
        showRequests = ShowRequests(restTemplate)
        movieRequests = MovieRequests(restTemplate)
        omdbApiStub = OmdbApiStub(stubServer)
    }

    @Test
    fun `should get all the saved shows times`() {
        //given
        val movieId = saveMovie()
        showRequests.saveShow(date = DATE_1, movieId = movieId)
        showRequests.saveShow(date = DATE_2, movieId = movieId)
        showRequests.saveShow(date = DATE_3, movieId = movieId)

        //when
        val showsResponse = showRequests.getShows()

        //then
        assertTrue(showsResponse.statusCode == OK)

        //and
        val shows = showsResponse.body!!.shows
        assertTrue(shows.size == 3)

        val showDates = shows.map { it.date }
        assertTrue(showDates.contains(DATE_1 + ZONE))
        assertTrue(showDates.contains(DATE_2 + ZONE))
        assertTrue(showDates.contains(DATE_3 + ZONE))
    }

    private fun saveMovie(): String {
        val imdbId = "imdb-id-1"
        omdbApiStub.stubOmdbApiOkResponse(imdbId)
        return movieRequests.saveMovie(imdbId).movieId()
    }

    private fun ResponseEntity<Any>.movieId(): String = (this.body as Map<*, *>)["movieId"].toString()
}

private const val DATE_1 = "2021-11-10T15:00"
private const val DATE_2 = "2021-11-10T18:00"
private const val DATE_3 = "2021-11-10T22:00"