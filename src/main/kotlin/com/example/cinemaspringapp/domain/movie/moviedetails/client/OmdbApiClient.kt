package com.example.cinemaspringapp.domain.movie.moviedetails.client

import com.example.cinemaspringapp.domain.movie.ImdbDetails
import com.example.cinemaspringapp.domain.movie.config.OmdbConfigurationProperties
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbId
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbMovieDetailsProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.web.util.UriComponentsBuilder

/**
 * OMDb API client
 * Used for fetching movie details from IMDb movie database
 *
 * Authentication: requires valid apikey parameter
 * Docs: https://www.omdbapi.com/
 */
open class OmdbApiClient(
    private val omdbConfig: OmdbConfigurationProperties,
    private val omdbApiHttpClient: OmdbApiHttpClient
) : ImdbMovieDetailsProvider {

    private val logger = KotlinLogging.logger {}

    @Retryable(
        value = [OmdbApiRetryableException::class],
        maxAttemptsExpression = "\${external-services.omdb.retry.maxAttempts}",
        backoff = Backoff(delayExpression = "\${external-services.omdb.retry.delayMs}")
    )
    override fun fetchImdbMovieDetails(imdbId: ImdbId): ImdbDetails? {
        logger.info { "[OmdbApiClient] Fetch movie details for imdbId: $imdbId" }

        val uri = omdbMovieDetailsUri(imdbId)
        return omdbApiHttpClient.exchangeOmdbRequest(uri, imdbId.value)
            ?.toDomain()
    }

    private fun OmdbApiMovieDetailsResponse.toDomain(): ImdbDetails =
        ImdbDetails(
            name = this.title ?: "",
            description = this.plot ?: "",
            releaseDate = this.released ?: "",
            rating = this.imdbRating ?: "",
            runtime = this.runtime ?: ""
        )

    private fun omdbMovieDetailsUri(imdbId: ImdbId): String =
        UriComponentsBuilder.fromUriString(omdbConfig.address)
            .queryParam("apikey", omdbConfig.apikey)
            .queryParam("i", imdbId.value)
            .toUriString()
}

