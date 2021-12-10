package com.example.cinemaspringapp.movie

import java.util.UUID

@JvmInline
value class MovieId(val value: String = UUID.randomUUID().toString())