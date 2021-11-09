package com.example.cinemaspringapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CinemaApp

fun main(args: Array<String>) {
    runApplication<CinemaApp>(*args)
}
