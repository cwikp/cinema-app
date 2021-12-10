package com.example.cinemaspringapp.show

interface ShowRepository {
    fun createShow(show: Show): Show
    fun updateShow(showId: ShowId, show: Show): Show
    fun findAll(): Collection<Show>
}