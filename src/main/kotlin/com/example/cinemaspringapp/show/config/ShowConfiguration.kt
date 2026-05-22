package com.example.cinemaspringapp.show.config

import com.example.cinemaspringapp.show.ShowFacade
import com.example.cinemaspringapp.show.ShowRepository
import com.example.cinemaspringapp.show.adapter.MongoShowRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoOperations

@Configuration
class ShowConfiguration {

    @Bean
    fun showFacade(showRepository: ShowRepository) =
        ShowFacade(showRepository)

    @Bean
    fun showRepository(mongoOperations: MongoOperations): ShowRepository =
        MongoShowRepository(mongoOperations)
}