package com.example.cinemaspringapp.domain.movie.moviedetails

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImdbIdTest {
    @Test
    fun `creates ImdbId for minimal length`() {
        val id = ImdbId("tt123")
        assertEquals("tt123", id.value)
    }

    @Test
    fun `creates ImdbId for maximal length`() {
        val id = ImdbId("ab12345678") // length 10
        assertEquals("ab12345678", id.value)
    }

    @Test
    fun `throws when too short`() {
        val ex = assertThrows(IllegalArgumentException::class.java) { ImdbId("t") }
        assertTrue(ex.message!!.contains("between 5 and 10"))
    }

    @Test
    fun `throws when too long`() {
        val ex = assertThrows(IllegalArgumentException::class.java) { ImdbId("ab123456789") } // length 11
        assertTrue(ex.message!!.contains("between 5 and 10"))
    }

    @Test
    fun `throws when prefix not letters`() {
        val ex = assertThrows(IllegalArgumentException::class.java) { ImdbId("1a123") }
        assertTrue(ex.message!!.contains("prefix with 2 letters"))
    }

    @Test
    fun `accepts uppercase letters in prefix`() {
        val id = ImdbId("TT123")
        assertEquals("TT123", id.value)
    }
}