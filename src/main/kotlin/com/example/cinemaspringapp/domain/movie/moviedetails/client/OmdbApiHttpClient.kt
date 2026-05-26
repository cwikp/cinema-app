package com.example.cinemaspringapp.domain.movie.moviedetails.client

import com.fasterxml.jackson.annotation.JsonAlias
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate

class OmdbApiHttpClient(private val omdbRestTemplate: RestTemplate) {
    fun exchangeOmdbRequest(uri: String, imdbId: String): OmdbApiMovieDetailsResponse? =
        try {
            omdbRestTemplate.getForEntity(uri, OmdbApiMovieDetailsResponse::class.java)
                .body!!
                .validateResponse(imdbId)
        } catch (ex: Exception) {
            handleException(ex, imdbId)
        }

    private fun handleException(ex: Exception, imdbId: String): Nothing {
        when {
            ex is HttpServerErrorException && ex.statusCode.is4xxClientError -> throw OmdbApiFormatException(
                imdbId = imdbId,
                msg = ex.message,
                ex = ex
            )

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