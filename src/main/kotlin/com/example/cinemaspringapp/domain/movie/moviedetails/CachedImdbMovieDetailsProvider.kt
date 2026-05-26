package com.example.cinemaspringapp.domain.movie.moviedetails

import com.example.cinemaspringapp.AllOpen
import com.example.cinemaspringapp.config.CacheKeys
import com.example.cinemaspringapp.domain.movie.ImdbDetails
import com.example.cinemaspringapp.domain.movie.moviedetails.client.OmdbApiClient
import org.springframework.cache.annotation.Cacheable

@AllOpen
class CachedImdbMovieDetailsProvider(private val omdbApiClient: OmdbApiClient) : ImdbMovieDetailsProvider {

    @Cacheable(value = [CacheKeys.IMDB_MOVIE_DETAILS], key = "#imdbId.value")
    override fun fetchImdbMovieDetails(imdbId: ImdbId): ImdbDetails? {
        return omdbApiClient.fetchImdbMovieDetails(imdbId)
    }
}