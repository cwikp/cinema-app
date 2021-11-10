package com.example.cinemaspringapp.show

import com.example.cinemaspringapp.movie.MovieId
import java.time.Instant

data class Show (
    val showId: ShowId,
    val name: ShowName,
    val movieId: MovieId,
    val date: Instant,
    val basePrice: Money
)