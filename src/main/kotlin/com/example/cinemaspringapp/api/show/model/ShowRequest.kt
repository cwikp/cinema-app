package com.example.cinemaspringapp.api.show.model

import java.time.LocalDateTime

data class CreateShowRequest(
    val name: String,
    val movieId: String,
    val date: DateRequest,
    val price: PriceRequest
)

data class UpdateShowRequest(
    val name: String,
    val movieId: String,
    val date: DateRequest,
    val price: PriceRequest
)

data class DateRequest(
    val localDateTime: LocalDateTime,
    val zoneId: String = DEFAULT_ZONE_ID,
)

data class PriceRequest(
    val basePrice: String,
    val currency: String = DEFAULT_CURRENCY
)

private const val DEFAULT_ZONE_ID = "Europe/Warsaw"
private const val DEFAULT_CURRENCY = "PLN"