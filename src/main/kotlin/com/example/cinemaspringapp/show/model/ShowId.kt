package com.example.cinemaspringapp.show.model

import java.util.UUID

@JvmInline
value class ShowId(val value: String = UUID.randomUUID().toString())