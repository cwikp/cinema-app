package com.example.cinemaspringapp.movie.client.imdb

import com.example.cinemaspringapp.movie.ImdbMovieDetails
import com.example.cinemaspringapp.movie.client.imdb.ImdbIdFactory.ImdbId

interface ImdbMovieDetailsProvider {
    fun fetchImdbMovieDetails(imdbId: ImdbId): ImdbMovieDetails?
}