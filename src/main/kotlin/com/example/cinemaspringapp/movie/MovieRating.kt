package com.example.cinemaspringapp.movie

import java.math.BigDecimal
import java.math.RoundingMode

data class MovieRating(val score: Long, val ratingsNumber: Long) {
    fun value(): String =
        if (ratingsNumber > 0) BigDecimal((score).toDouble() / ratingsNumber).setRatingScale().toString() else "0.0"

    companion object {
        fun initial() = MovieRating(0, 0)
        private fun BigDecimal.setRatingScale(): BigDecimal = setScale(1, RoundingMode.HALF_UP)
    }
}
