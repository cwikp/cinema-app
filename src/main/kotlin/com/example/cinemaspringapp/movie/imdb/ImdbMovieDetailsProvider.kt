package com.example.cinemaspringapp.movie.imdb

import com.example.cinemaspringapp.movie.ImdbMovieDetails
import com.example.cinemaspringapp.movie.imdb.ImdbIdFactory.ImdbId

interface ImdbMovieDetailsProvider {
    fun fetchImdbMovieDetails(imdbId: ImdbId): ImdbMovieDetails?
}