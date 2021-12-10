package com.example.cinemaspringapp.movie.imdb

interface ImdbIdValidator {
    fun isValidImdbId(maybeImdbId: String): Boolean
}