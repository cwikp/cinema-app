package com.example.cinemaspringapp.domain.show.model

import com.example.cinemaspringapp.domain.movie.model.MovieId

data class Show(
    val showId: ShowId,
    val name: ShowName,
    val movieId: MovieId,
    val date: ShowDate,
    val price: Money
)