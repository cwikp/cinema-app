package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.show.ShowRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shows")
class ShowEndpoint(private val showRepository: ShowRepository) {

    @GetMapping
    fun getShows(): ResponseEntity<ShowsResponse> =
        showRepository.findAll()
            .map { it.toResponse() }
            .let { ResponseEntity.ok(ShowsResponse(it)) }

    data class ShowsResponse(
        val shows: List<ShowResponse>
    )
}
