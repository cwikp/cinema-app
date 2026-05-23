package com.example.cinemaspringapp.domain.movie.moviedetails.client

import com.example.cinemaspringapp.domain.movie.ImdbDetails
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbId
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbMovieDetailsProvider
import com.example.cinemaspringapp.domain.movie.config.OmdbConfigurationProperties
import com.fasterxml.jackson.annotation.JsonAlias
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

/**
 * OMDb API client
 * Used for fetching movie details from IMDb movie database
 *
 * Authentication: requires valid apikey parameter
 * Docs: https://www.omdbapi.com/
 */
open class OmdbApiClient(
    private val omdbRestTemplate: RestTemplate,
    private val omdbConfig: OmdbConfigurationProperties
) : ImdbMovieDetailsProvider {

    private val logger = KotlinLogging.logger {}

    @Retryable(
        value = [OmdbApiRetryableException::class],
        maxAttemptsExpression = "\${external-services.omdb.retry.maxAttempts}",
        backoff = Backoff(delayExpression = "\${external-services.omdb.retry.delayMs}")
    )
    override fun fetchImdbMovieDetails(imdbId: ImdbId): ImdbDetails? {
        logger.info { "[OmdbApiClient] Fetch movie details for imdbId: $imdbId" }

        return exchangeImdbMovieDetailsRequest(imdbId.value)?.let {
            ImdbDetails(
                name = it.title ?: "",
                description = it.plot ?: "",
                releaseDate = it.released ?: "",
                rating = it.imdbRating ?: "",
                runtime = it.runtime ?: ""
            )
        }
    }

    private fun exchangeImdbMovieDetailsRequest(imdbId: String): OmdbApiMovieDetailsResponse? =
        try {
            omdbRestTemplate.getForEntity(omdbMovieDetailsUri(imdbId), OmdbApiMovieDetailsResponse::class.java)
                .body!!
                .validateResponse(imdbId)
        } catch (ex: Exception) {
            when {
                ex is HttpServerErrorException && ex.statusCode.is4xxClientError -> throw OmdbApiFormatException(imdbId, ex.message, ex)
                ex is HttpServerErrorException && ex.statusCode.is5xxServerError -> throw OmdbApiRetryableException(
                    "OmdbApi failed for imdbId: $imdbId, statusCode: ${ex.statusCode}", ex
                )

                else -> throw OmdbApiRetryableException("OmdbApi failed for imdbId: $imdbId", ex)
            }

        }

    private fun OmdbApiMovieDetailsResponse.validateResponse(imdbId: String): OmdbApiMovieDetailsResponse? =
        when {
            this.response.equals("true", ignoreCase = true) -> this //valid response
            this.response.equals("false", ignoreCase = true) && this.error == "Movie not found!" -> null
            else -> throw OmdbApiFormatException(imdbId, this.error)
        }

    private fun omdbMovieDetailsUri(imdbId: String) =
        UriComponentsBuilder.fromUriString(omdbConfig.address)
            .queryParam("apikey", omdbConfig.apikey)
            .queryParam("i", imdbId)
            .toUriString()
}

data class OmdbApiMovieDetailsResponse(
    @JsonAlias("Response") val response: String,
    @JsonAlias("Error") val error: String?,
    @JsonAlias("Title") val title: String?,
    @JsonAlias("Plot") val plot: String?,
    @JsonAlias("Released") val released: String?,
    val imdbRating: String?,
    @JsonAlias("Runtime") val runtime: String?
)

class OmdbApiFormatException(imdbId: String, msg: String?, ex: Exception? = null) :
    IllegalArgumentException("OmdbApi client error for imdbId: $imdbId, message: $msg", ex)

class OmdbApiRetryableException(msg: String, ex: Exception? = null) :
    IllegalStateException(msg, ex)