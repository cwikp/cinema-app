package com.example.cinemaspringapp.show

import java.time.Instant

data class Show (
    val showId: ShowId,
    val name: ShowName,
    val movieId: MovieId,
    val date: Instant,
    val basePrice: Money
)