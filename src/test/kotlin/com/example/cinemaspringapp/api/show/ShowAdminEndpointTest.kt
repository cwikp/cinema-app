package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.BaseIntegrationTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK

@TestInstance(PER_CLASS)
class ShowAdminEndpointTest : BaseIntegrationTest() {

    lateinit var showRequests: ShowRequests

    @BeforeAll
    fun initOnce() {
        showRequests = ShowRequests(restTemplate)
    }

    @Test
    fun `should save new show and update its date and price`() {
        //when
        val saveResponse = showRequests.saveShow()

        //then
        assertTrue(saveResponse.statusCode == CREATED)

        //and
        val showId = saveResponse.body!!.showId
        val shows = getAllShows()
        assertTrue(shows.size == 1)
        assertEquals(
            ShowResponse(
                showId = showId,
                name = SHOW_NAME,
                movieId = MOVIE_ID,
                date = MOVIE_DATE,
                basePrice = MOVIE_BASE_PRICE
            ),
            shows.first(),
        )

        //when
        val updatedDate = "2021-11-09T00:00:00Z"
        val updatedPrice = "29.99"
        val updateResponse = showRequests.updateShow(showId = showId, date = updatedDate, price = updatedPrice)

        //then
        assertTrue(updateResponse.statusCode == OK)

        //and
        val updatedShows = getAllShows()
        assertTrue(updatedShows.size == 1)
        assertEquals(
            ShowResponse(
                showId = showId,
                name = SHOW_NAME,
                movieId = MOVIE_ID,
                date = updatedDate,
                basePrice = updatedPrice
            ),
            updatedShows.first()
        )
    }

    private fun getAllShows() = showRequests.getShows().body!!.shows
}
