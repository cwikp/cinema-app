package com.example.cinemaspringapp.show

import com.example.cinemaspringapp.movie.MovieId

data class Show(
    val showId: ShowId,
    val name: ShowName,
    val movieId: MovieId,
    val date: ShowDate,
    val basePrice: Money
)