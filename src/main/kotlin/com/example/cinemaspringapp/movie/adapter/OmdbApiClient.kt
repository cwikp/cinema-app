package com.example.cinemaspringapp.movie.adapter

import com.example.cinemaspringapp.movie.ImdbMovieDetails
import com.example.cinemaspringapp.movie.config.OmdbConfigurationProperties
import com.example.cinemaspringapp.movie.imdb.ImdbIdFactory.ImdbId
import com.example.cinemaspringapp.movie.imdb.ImdbIdValidator
import com.example.cinemaspringapp.movie.imdb.ImdbMovieDetailsProvider
import com.fasterxml.jackson.annotation.JsonAlias
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class OmdbApiClient(
    private val omdbRestTemplate: RestTemplate,
    private val omdbConfig: OmdbConfigurationProperties
) : ImdbMovieDetailsProvider, ImdbIdValidator {

    override fun fetchImdbMovieDetails(imdbId: ImdbId): ImdbMovieDetails? =
        exchangeImdbMovieDetailsRequest(imdbId.value)?.let {
            ImdbMovieDetails(
                name = it.title ?: "",
                description = it.plot ?: "",
                releaseDate = it.released ?: "",
                rating = it.imdbRating ?: "",
                runtime = it.runtime ?: ""
            )
        }

    override fun isValidImdbId(maybeImdbId: String): Boolean =
        try {
            exchangeImdbMovieDetailsRequest(maybeImdbId)
            true
        } catch (ex: OmdbApiMovieDetailsNotFoundException) {
            false
        }

    private fun exchangeImdbMovieDetailsRequest(imdbId: String): OmdbApiMovieDetailsResponse? =
        try {
            val body = omdbRestTemplate.getForEntity(omdbFetchMovieDetailsUri(imdbId), OmdbApiMovieDetailsResponse::class.java).body!!
            when {
                body.response.toBoolean() -> body
                else -> throw OmdbApiMovieDetailsNotFoundException("Movie details for ImdbId: $imdbId not found, error: ${body.error}")
            }
        } catch (ex: Exception) {
            throw OmdbApiException("Cannot fetch details from omdbApi for imdbId: $imdbId", ex)
        }

    private fun omdbFetchMovieDetailsUri(imdbId: String) =
        UriComponentsBuilder.fromUriString(omdbConfig.address)
            .queryParam("apikey", omdbConfig.apikey)
            .queryParam("i", imdbId)
            .toUriString()

    data class OmdbApiMovieDetailsResponse(
        @JsonAlias("Response") val response: String,
        @JsonAlias("Error") val error: String?,
        @JsonAlias("Title") val title: String?,
        @JsonAlias("Plot") val plot: String?,
        @JsonAlias("Released") val released: String?,
        val imdbRating: String?,
        @JsonAlias("Runtime") val runtime: String?
    )
}

class OmdbApiException(msg: String, ex: Exception? = null) : RuntimeException(msg, ex)
class OmdbApiMovieDetailsNotFoundException(msg: String, ex: Exception? = null) : RuntimeException(msg, ex)
