package com.example.cinemaspringapp.api.show.model

import java.time.LocalDateTime

data class CreateShowApiRequest(
    val name: String,
    val movieId: String,
    val date: DateApiRequest,
    val price: PriceApiRequest
)

data class UpdateShowApiRequest(
    val name: String,
    val movieId: String,
    val date: DateApiRequest,
    val price: PriceApiRequest
)

data class DateApiRequest(
    val localDateTime: LocalDateTime,
    val zoneId: String = DEFAULT_ZONE_ID,
)

data class PriceApiRequest(
    val basePrice: String,
    val currency: String = DEFAULT_CURRENCY
)

private const val DEFAULT_ZONE_ID = "Europe/Warsaw"
private const val DEFAULT_CURRENCY = "PLN"