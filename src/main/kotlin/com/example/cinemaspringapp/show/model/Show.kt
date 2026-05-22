package com.example.cinemaspringapp.show.model

import com.example.cinemaspringapp.movie.model.MovieId

data class Show(
    val showId: ShowId,
    val name: ShowName,
    val movieId: MovieId,
    val date: ShowDate,
    val price: Money
)