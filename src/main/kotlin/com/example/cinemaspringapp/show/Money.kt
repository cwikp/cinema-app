package com.example.cinemaspringapp.show

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

data class Money private constructor (private val originalValue: BigDecimal, val currency: Currency) {
    val value: String = originalValue.setScale(2, RoundingMode.HALF_UP).toString()

    init {
        require(originalValue >= BigDecimal.ZERO) { "Money cannot be negative" }
    }

    companion object {
        fun money(value: String, currency: String) = Money(BigDecimal(value).setMoneyScale(), Currency.getInstance(currency))

        private fun BigDecimal.setMoneyScale(): BigDecimal = setScale(8, RoundingMode.HALF_UP)
    }
}