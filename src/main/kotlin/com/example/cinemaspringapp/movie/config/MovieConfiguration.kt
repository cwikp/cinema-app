package com.example.cinemaspringapp.movie.config

import com.example.cinemaspringapp.movie.MovieFacade
import com.example.cinemaspringapp.movie.MovieRepository
import com.example.cinemaspringapp.movie.adapter.MOVIE_DETAILS_CACHE_NAME
import com.example.cinemaspringapp.movie.adapter.MongoMovieRepository
import com.example.cinemaspringapp.movie.adapter.OmdbApiClient
import com.example.cinemaspringapp.movie.imdb.ImdbIdFactory
import com.example.cinemaspringapp.movie.imdb.ImdbIdValidator
import com.example.cinemaspringapp.movie.imdb.ImdbMovieDetailsProvider
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
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
    fun imdbIdFactory(imdbIdValidator: ImdbIdValidator) = ImdbIdFactory(imdbIdValidator)

    @Bean
    fun omdbApiClient(restTemplateBuilder: RestTemplateBuilder, omdbConfig: OmdbConfigurationProperties): OmdbApiClient =
        OmdbApiClient(omdbRestTemplate(restTemplateBuilder, omdbConfig.connection), omdbConfig)

    private fun omdbRestTemplate(restTemplateBuilder: RestTemplateBuilder, connection: ConnectionProperties): RestTemplate =
        restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(connection.connectTimeoutMillis))
            .setReadTimeout(Duration.ofMillis(connection.socketTimeoutMillis))
            .build()

    @Bean
    fun cacheManager(@Value("\${cache.movieDetails}") movieDetailsCacheConfig: String): CacheManager {
        val cacheList = listOf(
            CaffeineCache(MOVIE_DETAILS_CACHE_NAME, Caffeine.from(movieDetailsCacheConfig).build(), false),
        )
        return SimpleCacheManager().apply {
            setCaches(cacheList)
        }
    }
}

@ConstructorBinding
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