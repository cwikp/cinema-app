package com.example.cinemaspringapp.api

import com.example.cinemaspringapp.BaseIntegrationTest
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.http.HttpStatus.OK

@TestInstance(PER_CLASS)
class ShowEndpointTest : BaseIntegrationTest() {

    lateinit var showRequests: ShowRequests

    @BeforeAll
    fun initOnce() {
        showRequests = ShowRequests(restTemplate)
    }

    @Test
    fun `should get all the saved shows times`() {
        //given
        showRequests.saveShow(date = DATE_1)
        showRequests.saveShow(date = DATE_2)
        showRequests.saveShow(date = DATE_3)

        //when
        val showsResponse = showRequests.getShows()

        //then
        assertTrue(showsResponse.statusCode == OK)

        //and
        val shows = showsResponse.body!!.shows
        assertTrue(shows.size == 3)

        val showDates = shows.map { it.date }
        assertTrue(showDates.contains(DATE_1))
        assertTrue(showDates.contains(DATE_2))
        assertTrue(showDates.contains(DATE_3))
    }
}

private const val DATE_1 = "2021-11-10T15:00:00Z"
private const val DATE_2 = "2021-11-10T18:00:00Z"
private const val DATE_3 = "2021-11-10T22:00:00Z"
