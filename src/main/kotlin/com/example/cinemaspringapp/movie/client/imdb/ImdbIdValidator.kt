package com.example.cinemaspringapp.movie.client.imdb

interface ImdbIdValidator {
    fun isValidImdbId(maybeImdbId: String): Boolean
}