package com.example.cinemaspringapp.movie.adapter

import com.example.cinemaspringapp.movie.imdb.ImdbIdFactory
import com.example.cinemaspringapp.movie.Movie
import com.example.cinemaspringapp.movie.MovieId
import com.example.cinemaspringapp.movie.MovieRating
import com.example.cinemaspringapp.movie.MovieRepository
import com.example.cinemaspringapp.movie.UserMovieRating
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

    override fun storeRating(userMovieRating: UserMovieRating): Movie {
        val result = mongoOperations.updateFirst(
            query(where(MOVIE_ID).isEqualTo(userMovieRating.movieId.value)),
            Update().apply {
                inc(SCORE, userMovieRating.rating)
                inc(RATINGS_NUMBER)
            },
            MovieDocument::class.java
        )
        check(result.wasAcknowledged()) { "Could not update score rating for movieId: ${userMovieRating.movieId.value}" }
        return findMovie(userMovieRating.movieId)!!
    }
}

private fun Movie.toDocument() = MovieDocument(
    movieId = movieId.value,
    imdbId = imdbId.value,
    score = rating.score,
    ratingsNumber = rating.ratingsNumber
)

private fun MovieDocument.toDomain() = Movie(
    movieId = MovieId(movieId),
    imdbId = ImdbIdFactory.ImdbId.validated(imdbId),
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
