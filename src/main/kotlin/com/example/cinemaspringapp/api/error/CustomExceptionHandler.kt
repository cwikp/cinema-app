package com.example.cinemaspringapp.api.error

import com.example.cinemaspringapp.domain.movie.exception.ImdbMovieNotFoundException
import com.example.cinemaspringapp.domain.movie.exception.MovieNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class CustomExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException) =
        ex.handleAsBadRequest()

    @ExceptionHandler(ImdbMovieNotFoundException::class)
    fun handleImdbMovieNotFoundException(ex: ImdbMovieNotFoundException) =
        ex.handleAsBadRequest()

    @ExceptionHandler(MovieNotFoundException::class)
    fun handleMovieNotFoundException(ex: MovieNotFoundException) =
        ex.handleAsNotFound()

    fun Throwable.handleAsBadRequest(): ResponseEntity<ErrorBody> {
        logger.error { "Bad request: ${this.message}" }
        return ResponseEntity
            .badRequest()
            .body(ErrorBody(this.message))
    }

    fun Throwable.handleAsNotFound(): ResponseEntity<ErrorBody> {
        return ResponseEntity
            .notFound()
            .build()
    }
}

data class ErrorBody(val message: String?)