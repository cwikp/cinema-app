package com.example.cinemaspringapp.show

import java.math.BigDecimal
import java.math.RoundingMode

data class Money private constructor (private val originalValue: BigDecimal) {
    val value: String = originalValue.setScale(2, RoundingMode.HALF_UP).toString()

    init {
        require(originalValue >= BigDecimal.ZERO) { "Money cannot be negative" }
    }

    companion object {
        fun money(value: String) = Money(BigDecimal(value).setMoneyScale())

        private fun BigDecimal.setMoneyScale(): BigDecimal = setScale(8, RoundingMode.HALF_UP)
    }
}