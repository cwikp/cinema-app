package com.example.cinemaspringapp.show

import com.example.cinemaspringapp.show.model.Show
import com.example.cinemaspringapp.show.model.ShowId

class ShowFacade(
    private val showRepository: ShowRepository,
) {

    fun createShow(show: Show): Show =
        showRepository.createShow(show)

    fun updateShow(showId: ShowId, show: Show): Show =
        showRepository.updateShow(showId, show)

    fun allShows(): Collection<Show> =
        showRepository.findAll()
}

