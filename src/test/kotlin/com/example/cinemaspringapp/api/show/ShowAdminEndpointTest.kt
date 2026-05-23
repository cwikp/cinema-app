package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.BaseIntegrationTest
import com.example.cinemaspringapp.api.movie.MovieRequests
import com.example.cinemaspringapp.api.show.model.ShowApiResponse
import com.example.cinemaspringapp.stub.OmdbApiStub
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

@TestInstance(PER_CLASS)
class ShowAdminEndpointTest : BaseIntegrationTest() {

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
    fun `should save new show and update its date and price`() {
        //given
        val movieId = saveMovie()

        //when
        val saveResponse = showRequests.saveShow(movieId = movieId)

        //then
        assertTrue(saveResponse.statusCode == CREATED)

        //and
        val showId = saveResponse.body!!.showId
        val shows = getAllShows()
        assertTrue(shows.size == 1)
        assertEquals(
            ShowApiResponse(
                showId = showId,
                name = SHOW_NAME,
                movieId = movieId,
                date = MOVIE_LOCAL_DATE_TIME + ZONE,
                basePrice = MOVIE_BASE_PRICE
            ),
            shows.first(),
        )

        //when
        val updatedDate = "2021-11-09T00:00"
        val updatedPrice = "29.99"
        val updateResponse = showRequests.updateShow(showId = showId, movieId = movieId, date = updatedDate, price = updatedPrice)

        //then
        assertTrue(updateResponse.statusCode == OK)

        //and
        val updatedShows = getAllShows()
        assertTrue(updatedShows.size == 1)
        assertEquals(
            ShowApiResponse(
                showId = showId,
                name = SHOW_NAME,
                movieId = movieId,
                date = updatedDate + ZONE,
                basePrice = updatedPrice
            ),
            updatedShows.first()
        )
    }

    private fun saveMovie(): String {
        val imdbId = "imdb-id-1"
        omdbApiStub.stubOmdbApiOkResponse(imdbId)
        return movieRequests.saveMovie(imdbId).movieId()
    }

    private fun getAllShows() = showRequests.getShows().body!!.shows
    private fun ResponseEntity<Any>.movieId(): String = (this.body as Map<*, *>)["movieId"].toString()
}
