package com.example.cinemaspringapp.domain.movie.repository

import com.example.cinemaspringapp.domain.movie.model.MovieId
import com.example.cinemaspringapp.domain.movie.model.MovieRating
import com.example.cinemaspringapp.domain.movie.model.MovieSingleRating
import com.example.cinemaspringapp.domain.movie.moviedetails.ImdbId
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo

class MongoMovieRepository(private val mongoOperations: MongoOperations) : MovieRepository {

    override fun createMovie(movie: Movie): Movie =
        movie.toDocument()
            .let { mongoOperations.save(it) }
            .toDomain()

    override fun findMovie(movieId: MovieId): Movie? =
        mongoOperations.findOne(
            query(where(MOVIE_ID).isEqualTo(movieId.value)),
            MovieDocument::class.java
        )?.toDomain()


    // note that even if though the update operation is atomic, some race condition may happen as we do not check current ratings_number
    // also we do not store rating per user
    // this is good enough if we do not care about strong consistency on ratings, but we have to be aware of its limitations
    override fun storeRating(movieSingleRating: MovieSingleRating): Movie {
        val result = mongoOperations.updateFirst(
            query(where(MOVIE_ID).isEqualTo(movieSingleRating.movieId.value)),
            Update().apply {
                inc(SCORE, movieSingleRating.singleRating.value)
                inc(RATINGS_NUMBER)
            },
            MovieDocument::class.java
        )
        check(result.wasAcknowledged()) { "Could not update score rating for movieId: ${movieSingleRating.movieId.value}" }
        return findMovie(movieSingleRating.movieId)!!
    }
}

private fun Movie.toDocument() = MovieDocument(
    movieId = movieId.value.toString(),
    imdbId = imdbId.value,
    score = rating.score,
    ratingsNumber = rating.ratingsNumber
)

private fun MovieDocument.toDomain() = Movie(
    movieId = MovieId.fromString(movieId),
    imdbId = ImdbId(imdbId),
    rating = MovieRating(score, ratingsNumber)
)


@Document(collection = "movies")
data class MovieDocument(
    @field:Indexed(unique = true) val movieId: String,
    @field:Indexed(unique = true) val imdbId: String,
    val score: Long,
    val ratingsNumber: Long
)

private const val MOVIE_ID = "movieId"
private const val IMDB_ID = "imdbId"
private const val SCORE = "score"
private const val RATINGS_NUMBER = "ratingsNumber"
