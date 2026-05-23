package com.example.cinemaspringapp.domain.movie.exception

class ImdbMovieNotFoundException(id: String) : IllegalArgumentException("IMDb movie details not found for id: $id")
