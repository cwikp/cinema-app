package com.example.cinemaspringapp.domain.show

import com.example.cinemaspringapp.domain.movie.MovieFacade
import com.example.cinemaspringapp.domain.movie.exception.MovieNotFoundException
import com.example.cinemaspringapp.domain.show.model.Show
import io.github.oshai.kotlinlogging.KotlinLogging

class ShowFacade(
    private val showRepository: ShowRepository,
    private val movieFacade: MovieFacade
) {

    private val logger = KotlinLogging.logger {}

    fun createShow(show: Show): Show {
        logger.info { "Create show request: $show" }
        movieFacade.findMovie(show.movieId) ?: throw MovieNotFoundException(show.movieId)

        return showRepository.createShow(show)
    }

    fun updateShow(show: Show): Show {
        logger.info { "Update show request: $show" }
        movieFacade.findMovie(show.movieId) ?: throw MovieNotFoundException(show.movieId)

        return showRepository.updateShow(show)
    }

    // note: returning all shows which is good enough for start
    // but real implementation would need pagination
    fun allShows(): Collection<Show> =
        showRepository.findAll()
}

