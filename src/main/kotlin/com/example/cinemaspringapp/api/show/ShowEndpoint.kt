package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.api.show.model.ShowApiResponse
import com.example.cinemaspringapp.api.show.model.toApiResponse
import com.example.cinemaspringapp.domain.show.ShowFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shows")
class ShowEndpoint(private val showFacade: ShowFacade) {

    @GetMapping
    fun getShows(): ResponseEntity<ShowsApiResponse> =
        showFacade.allShows()
            .map { it.toApiResponse() }
            .let { ResponseEntity.ok(ShowsApiResponse(shows = it)) }
}

data class ShowsApiResponse(
    val shows: List<ShowApiResponse>
)