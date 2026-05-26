package com.example.cinemaspringapp.api.show.model

import com.example.cinemaspringapp.domain.show.model.Show

data class ShowApiResponse(
    val showId: String,
    val name: String,
    val movieId: String,
    val date: String,
    val basePrice: String
)

fun Show.toApiResponse() = ShowApiResponse(
    showId = showId.value,
    name = name.value,
    movieId = movieId.value,
    date = date.zonedDate(),
    basePrice = price.value
)