package com.example.cinemaspringapp.api.error

import com.example.cinemaspringapp.movie.adapter.OmdbApiMovieDetailsNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException) =
        ex.handleAsBadRequest()

    @ExceptionHandler(OmdbApiMovieDetailsNotFoundException::class)
    fun handleOmdbApiMovieDetailsNotFoundException(ex: OmdbApiMovieDetailsNotFoundException) =
        ex.handleAsBadRequest()

    fun Throwable.handleAsBadRequest(): ResponseEntity<ErrorBody> {
        return ResponseEntity
            .badRequest()
            .body(ErrorBody(this.message))
    }
}

data class ErrorBody(val message: String?)