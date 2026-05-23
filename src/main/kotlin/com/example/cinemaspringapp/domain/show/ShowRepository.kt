package com.example.cinemaspringapp.domain.show

import com.example.cinemaspringapp.domain.show.model.Show

interface ShowRepository {
    fun createShow(show: Show): Show
    fun updateShow(show: Show): Show
    fun findAll(): Collection<Show>
}