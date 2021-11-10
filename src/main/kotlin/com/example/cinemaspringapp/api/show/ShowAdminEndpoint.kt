package com.example.cinemaspringapp.api.show

import com.example.cinemaspringapp.show.Money.Companion.money
import com.example.cinemaspringapp.movie.MovieId
import com.example.cinemaspringapp.show.Show
import com.example.cinemaspringapp.show.ShowId
import com.example.cinemaspringapp.show.ShowName
import com.example.cinemaspringapp.show.ShowRepository
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/admin/shows")
class ShowAdminEndpoint(private val showRepository: ShowRepository) {

    @PostMapping
    fun createShow(@RequestBody createShowRequest: CreateShowRequest): ResponseEntity<ShowResponse> =
        createShowRequest.toDomain()
            .let { showRepository.createShow(it) }
            .let { ResponseEntity.status(CREATED).body(it.toResponse()) }

    @PutMapping("/{showId}")
    fun updateShow(@PathVariable showId: String, @RequestBody updateShowRequest: UpdateShowRequest) =
        updateShowRequest.toDomain(showId)
            .let { showRepository.updateShow(ShowId(showId), it) }
            .let { ResponseEntity.ok(it.toResponse()) }

    private fun CreateShowRequest.toDomain() = Show(
        showId = ShowId(),
        name = ShowName(name),
        movieId = MovieId(movieId),
        date = date,
        basePrice = money(price)
    )

    private fun UpdateShowRequest.toDomain(showId: String) = Show(
        showId = ShowId(showId),
        name = ShowName(name),
        movieId = MovieId(movieId),
        date = date,
        basePrice = money(price)
    )

    data class CreateShowRequest(
        val name: String,
        val movieId: String,
        val date: Instant,
        val price: String
    )

    data class UpdateShowRequest(
        val name: String,
        val movieId: String,
        val date: Instant,
        val price: String
    )
}
