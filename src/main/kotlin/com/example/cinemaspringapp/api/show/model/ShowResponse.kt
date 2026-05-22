package com.example.cinemaspringapp.api.show.model

import com.example.cinemaspringapp.show.model.Show

data class ShowResponse(
    val showId: String,
    val name: String,
    val movieId: String,
    val date: String,
    val basePrice: String
)

fun Show.toResponse() = ShowResponse(
    showId = showId.value,
    name = name.value,
    movieId = movieId.value,
    date = date.zonedDate(),
    basePrice = price.value
)