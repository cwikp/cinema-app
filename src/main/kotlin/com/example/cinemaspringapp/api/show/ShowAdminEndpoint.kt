package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.api.show.model.CreateShowApiRequest
import com.example.cinemaspringapp.api.show.model.ShowApiResponse
import com.example.cinemaspringapp.api.show.model.UpdateShowApiRequest
import com.example.cinemaspringapp.api.show.model.toApiResponse
import com.example.cinemaspringapp.domain.movie.model.MovieId
import com.example.cinemaspringapp.domain.show.ShowFacade
import com.example.cinemaspringapp.domain.show.model.Money.Companion.money
import com.example.cinemaspringapp.domain.show.model.Show
import com.example.cinemaspringapp.domain.show.model.ShowDate
import com.example.cinemaspringapp.domain.show.model.ShowId
import com.example.cinemaspringapp.domain.show.model.ShowName
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneId

@RestController
@RequestMapping("/api/admin/shows")
class ShowAdminEndpoint(private val showFacade: ShowFacade) {

    @PostMapping
    fun createShow(@RequestBody request: CreateShowApiRequest): ResponseEntity<ShowApiResponse> {
        val show = request.toDomain()
        return showFacade.createShow(show)
            .let { ResponseEntity.status(CREATED).body(it.toApiResponse()) }
    }

    @PutMapping("/{showId}")
    fun updateShow(@PathVariable showId: String, @RequestBody updateShowApiRequest: UpdateShowApiRequest): ResponseEntity<ShowApiResponse?> {
        val show = updateShowApiRequest.toDomain(showId)
        return showFacade.updateShow(show)
            .let { ResponseEntity.ok(it.toApiResponse()) }
    }
}

private fun CreateShowApiRequest.toDomain() = Show(
    showId = ShowId(),
    name = ShowName.fromString(name),
    movieId = MovieId.fromString(movieId),
    date = ShowDate(date.localDateTime, ZoneId.of(date.zoneId)),
    price = money(price.basePrice, price.currency)
)

private fun UpdateShowApiRequest.toDomain(showId: String) = Show(
    showId = ShowId.fromString(showId),
    name = ShowName.fromString(name),
    movieId = MovieId.fromString(movieId),
    date = ShowDate(date.localDateTime, ZoneId.of(date.zoneId)),
    price = money(price.basePrice, price.currency)
)

