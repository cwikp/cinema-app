package com.example.cinemaspringapp.domain.movie.moviedetails

import com.example.cinemaspringapp.domain.movie.ImdbDetails

interface ImdbMovieDetailsProvider {
    fun fetchImdbMovieDetails(imdbId: ImdbId): ImdbDetails?
}