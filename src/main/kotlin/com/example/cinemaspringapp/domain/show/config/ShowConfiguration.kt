package com.example.cinemaspringapp.domain.show.config

import com.example.cinemaspringapp.domain.movie.MovieFacade
import com.example.cinemaspringapp.domain.show.ShowFacade
import com.example.cinemaspringapp.domain.show.ShowRepository
import com.example.cinemaspringapp.domain.show.adapter.MongoShowRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoOperations

@Configuration
class ShowConfiguration {

    @Bean
    fun showFacade(showRepository: ShowRepository, movieFacade: MovieFacade) =
        ShowFacade(showRepository, movieFacade)

    @Bean
    fun showRepository(mongoOperations: MongoOperations): ShowRepository =
        MongoShowRepository(mongoOperations)
}