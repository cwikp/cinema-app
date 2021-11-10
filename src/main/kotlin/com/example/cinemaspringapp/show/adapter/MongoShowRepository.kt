package com.example.cinemaspringapp.show.adapter

import com.example.cinemaspringapp.show.Money
import com.example.cinemaspringapp.movie.MovieId
import com.example.cinemaspringapp.show.Show
import com.example.cinemaspringapp.show.ShowId
import com.example.cinemaspringapp.show.ShowName
import com.example.cinemaspringapp.show.ShowRepository
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import java.time.Instant

class MongoShowRepository(private val mongoOperations: MongoOperations) : ShowRepository {

    override fun createShow(show: Show): Show =
        mongoOperations.save(show.toDocument()).toDomain()

    override fun updateShow(showId: ShowId, show: Show): Show {
        val result = mongoOperations.updateFirst(
            query(where(SHOW_ID).isEqualTo(showId.value)),
            Update().apply {
                set(NAME, show.name.value)
                set(MOVIE_ID, show.movieId.value)
                set(DATE, show.date)
                set(BASE_PRICE, show.basePrice.value)
            },
            ShowDocument::class.java
        )
        check(result.wasAcknowledged()) { "Could not update show with id: $showId" }
        return findOne(showId)!!
    }

    override fun findAll(): Collection<Show> =
        mongoOperations.findAll(ShowDocument::class.java).map { it.toDomain() }

    private fun findOne(showId: ShowId): Show? =
        mongoOperations.findOne(
            query(where(SHOW_ID).isEqualTo(showId.value)),
            ShowDocument::class.java
        )?.toDomain()
}

@Document(collection = "shows")
@CompoundIndex(name = "movieId_date_name", def = "{'$MOVIE_ID': 1, '$DATE': 1, '$NAME': 1}", unique = true)
data class ShowDocument(
    @field:Indexed(unique = true) val showId: String,
    val name: String,
    val movieId: String,
    val date: Instant,
    val basePrice: String
)

private fun ShowDocument.toDomain() = Show(
    showId = ShowId(showId),
    name = ShowName(name),
    movieId = MovieId(movieId),
    date = date,
    basePrice = Money.money(basePrice)
)

private fun Show.toDocument() = ShowDocument(
    showId = showId.value,
    name = name.value,
    movieId = movieId.value,
    date = date,
    basePrice = basePrice.value
)

private const val SHOW_ID = "showId"
private const val NAME = "name"
private const val MOVIE_ID = "movieId"
private const val DATE = "date"
private const val BASE_PRICE = "basePrice"