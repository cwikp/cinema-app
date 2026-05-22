package com.example.cinemaspringapp.show

import com.example.cinemaspringapp.show.model.Show
import com.example.cinemaspringapp.show.model.ShowId

interface ShowRepository {
    fun createShow(show: Show): Show
    fun updateShow(showId: ShowId, show: Show): Show
    fun findAll(): Collection<Show>
}