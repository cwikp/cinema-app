package com.example.cinemaspringapp.domain.movie.exception

import com.example.cinemaspringapp.domain.movie.model.MovieId

class MovieNotFoundException(id: MovieId) : IllegalArgumentException("Movie not found for id: $id")
