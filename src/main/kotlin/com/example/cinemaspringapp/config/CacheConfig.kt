package com.example.cinemaspringapp.config

import com.example.cinemaspringapp.config.CacheKeys.IMDB_MOVIE_DETAILS
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(@Value("\${cache.movieDetails}") movieDetailsCacheConfig: String): CacheManager {
        val cacheList = listOf(
            CaffeineCache(IMDB_MOVIE_DETAILS, Caffeine.from(movieDetailsCacheConfig).build(), false),
        )
        return SimpleCacheManager().apply {
            setCaches(cacheList)
        }
    }
}

object CacheKeys {
    const val IMDB_MOVIE_DETAILS = "movie-details"
}