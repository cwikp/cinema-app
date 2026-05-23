package com.example.cinemaspringapp.domain.show

import com.example.cinemaspringapp.domain.movie.MovieFacade
import com.example.cinemaspringapp.domain.movie.exception.MovieNotFoundException
import com.example.cinemaspringapp.domain.movie.model.MovieId
import com.example.cinemaspringapp.domain.movie.model.MovieRating
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbId
import com.example.cinemaspringapp.domain.movie.repository.Movie
import com.example.cinemaspringapp.domain.show.model.Money
import com.example.cinemaspringapp.domain.show.model.Show
import com.example.cinemaspringapp.domain.show.model.ShowDate
import com.example.cinemaspringapp.domain.show.model.ShowId
import com.example.cinemaspringapp.domain.show.model.ShowName
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId

class ShowFacadeTest {
    private val showRepository = mockk<ShowRepository>()
    private val movieFacade = mockk<MovieFacade>()

    private val showFacade = ShowFacade(showRepository, movieFacade)

    @Test
    fun `createShow delegates to repository when movie exists`() {
        every { movieFacade.findMovie(any()) } returns movie
        every { showRepository.createShow(show) } returns show

        val result = showFacade.createShow(show)

        assertSame(show, result)
        verify(exactly = 1) { movieFacade.findMovie(any()) }
        verify(exactly = 1) { showRepository.createShow(show) }
    }

    @Test
    fun `updateShow delegates to repository when movie exists`() {
        every { movieFacade.findMovie(any()) } returns movie
        every { showRepository.updateShow(show) } returns show

        val result = showFacade.updateShow(show)

        assertSame(show, result)
        verify(exactly = 1) { movieFacade.findMovie(any()) }
        verify(exactly = 1) { showRepository.updateShow(show) }
    }

    @Test
    fun `allShows returns repository results`() {
        val shows = listOf(show)
        every { showRepository.findAll() } returns shows

        val result = showFacade.allShows()

        assertEquals(shows, result)
        verify(exactly = 1) { showRepository.findAll() }
    }

    @Test
    fun `createShow throws MovieNotFoundException when movie missing`() {
        every { movieFacade.findMovie(any()) } returns null

        Assertions.assertThrows(MovieNotFoundException::class.java) {
            showFacade.createShow(show)
        }
    }

    @Test
    fun `updateShow throws MovieNotFoundException when movie missing`() {
        every { movieFacade.findMovie(any()) } returns null

        Assertions.assertThrows(MovieNotFoundException::class.java) {
            showFacade.updateShow(show)
        }
    }
}

private val movieId = MovieId()
private val movie = Movie(
    movieId = movieId,
    imdbId = ImdbId("aa123"),
    rating = MovieRating(10, 1)
)

private val show = Show(
    showId = ShowId(),
    name = ShowName.fromString("Test show"),
    movieId = movieId,
    date = ShowDate(LocalDateTime.now(), ZoneId.of("Europe/Warsaw")),
    price = Money.money("10.11", "PLN")
)