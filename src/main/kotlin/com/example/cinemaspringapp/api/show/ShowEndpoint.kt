package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.api.show.model.ShowResponse
import com.example.cinemaspringapp.api.show.model.toResponse
import com.example.cinemaspringapp.show.ShowFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shows")
class ShowEndpoint(private val showFacade: ShowFacade) {

    @GetMapping
    fun getShows(): ResponseEntity<ShowsResponse> =
        showFacade.allShows()
            .map { it.toResponse() }
            .let { ResponseEntity.ok(ShowsResponse(it)) }

    data class ShowsResponse(
        val shows: List<ShowResponse>
    )
}
