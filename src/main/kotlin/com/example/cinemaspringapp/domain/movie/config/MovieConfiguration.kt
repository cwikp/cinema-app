package com.example.cinemaspringapp.domain.movie.config

import com.example.cinemaspringapp.domain.movie.MovieFacade
import com.example.cinemaspringapp.domain.movie.moviedetails.CachedImdbMovieDetailsProvider
import com.example.cinemaspringapp.domain.movie.moviedetails.client.OmdbApiClient
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbMovieDetailsProvider
import com.example.cinemaspringapp.domain.movie.moviedetails.client.OmdbApiHttpClient
import com.example.cinemaspringapp.domain.movie.repository.MongoMovieRepository
import com.example.cinemaspringapp.domain.movie.repository.MovieRepository
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
@EnableConfigurationProperties(OmdbConfigurationProperties::class)
class MovieConfiguration {

    @Bean
    fun movieFacade(movieRepository: MovieRepository, imdbMovieDetailsProvider: ImdbMovieDetailsProvider) =
        MovieFacade(movieRepository, imdbMovieDetailsProvider)

    @Bean
    fun movieRepository(mongoOperations: MongoOperations): MovieRepository =
        MongoMovieRepository(mongoOperations)

    @Bean
    fun imdbMovieDetailsProvider(
        restTemplateBuilder: RestTemplateBuilder,
        omdbConfig: OmdbConfigurationProperties
    ): ImdbMovieDetailsProvider {
        val omdbApiHttpClient = OmdbApiHttpClient(
            omdbRestTemplate(restTemplateBuilder, omdbConfig.connection)
        )
        val omdbApiClient = OmdbApiClient(
            omdbApiHttpClient = omdbApiHttpClient,
            omdbConfig = omdbConfig
        )
        return CachedImdbMovieDetailsProvider(omdbApiClient)
    }

    private fun omdbRestTemplate(
        restTemplateBuilder: RestTemplateBuilder,
        connection: ConnectionProperties
    ): RestTemplate =
        restTemplateBuilder
            .connectTimeout(Duration.ofMillis(connection.connectTimeoutMillis))
            .readTimeout(Duration.ofMillis(connection.socketTimeoutMillis))
            .build()
}

@ConfigurationProperties("external-services.omdb")
data class OmdbConfigurationProperties(
    val address: String,
    val apikey: String,
    val connection: ConnectionProperties
)

data class ConnectionProperties(
    val connectTimeoutMillis: Long,
    val socketTimeoutMillis: Long
)