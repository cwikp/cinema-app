package com.example.cinemaspringapp.show

import java.util.UUID

@JvmInline
value class MovieId(val value: String = UUID.randomUUID().toString())