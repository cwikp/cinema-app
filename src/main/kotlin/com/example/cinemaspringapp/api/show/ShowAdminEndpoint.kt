package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.api.show.model.CreateShowRequest
import com.example.cinemaspringapp.api.show.model.ShowResponse
import com.example.cinemaspringapp.api.show.model.UpdateShowRequest
import com.example.cinemaspringapp.api.show.model.toResponse
import com.example.cinemaspringapp.movie.model.MovieId
import com.example.cinemaspringapp.show.ShowFacade
import com.example.cinemaspringapp.show.model.Money.Companion.money
import com.example.cinemaspringapp.show.model.Show
import com.example.cinemaspringapp.show.model.ShowDate
import com.example.cinemaspringapp.show.model.ShowId
import com.example.cinemaspringapp.show.model.ShowName
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneId

@RestController
@RequestMapping("/api/admin/shows")
class ShowAdminEndpoint(private val showFacade: ShowFacade) {

    @PostMapping
    fun createShow(@RequestBody createShowRequest: CreateShowRequest): ResponseEntity<ShowResponse> =
        createShowRequest.toDomain()
            .let { showFacade.createShow(it) }
            .let { ResponseEntity.status(CREATED).body(it.toResponse()) }

    @PutMapping("/{showId}")
    fun updateShow(@PathVariable showId: String, @RequestBody updateShowRequest: UpdateShowRequest) =
        updateShowRequest.toDomain(showId)
            .let { showFacade.updateShow(ShowId(showId), it) }
            .let { ResponseEntity.ok(it.toResponse()) }
}

private fun CreateShowRequest.toDomain() = Show(
    showId = ShowId(),
    name = ShowName(name),
    movieId = MovieId(movieId),
    date = ShowDate(date.localDateTime, ZoneId.of(date.zoneId)),
    price = money(price.basePrice, price.currency)
)

private fun UpdateShowRequest.toDomain(showId: String) = Show(
    showId = ShowId(showId),
    name = ShowName(name),
    movieId = MovieId(movieId),
    date = ShowDate(date.localDateTime, ZoneId.of(date.zoneId)),
    price = money(price.basePrice, price.currency)
)

