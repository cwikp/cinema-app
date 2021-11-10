package com.example.cinemaspringapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class CinemaApp

fun main(args: Array<String>) {
    runApplication<CinemaApp>(*args)
}

annotation class AllOpen