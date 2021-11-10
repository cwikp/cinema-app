package com.example.cinemaspringapp.movie.config

import com.example.cinemaspringapp.movie.MovieFacade
import com.example.cinemaspringapp.movie.MovieRepository
import com.example.cinemaspringapp.movie.adapter.MongoMovieRepository
import com.example.cinemaspringapp.movie.adapter.OmdbApiClient
import com.example.cinemaspringapp.movie.imdb.ImdbIdFactory
import com.example.cinemaspringapp.movie.imdb.ImdbIdValidator
import com.example.cinemaspringapp.movie.imdb.ImdbMovieDetailsProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.web.client.RestTemplate

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
        OmdbApiClient(omdbRestTemplate(restTemplateBuilder), omdbConfig)

    private fun omdbRestTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate =
        restTemplateBuilder.build()
}

@ConstructorBinding
@ConfigurationProperties("external-services.omdb")
data class OmdbConfigurationProperties(
    val address: String,
    val apikey: String
)