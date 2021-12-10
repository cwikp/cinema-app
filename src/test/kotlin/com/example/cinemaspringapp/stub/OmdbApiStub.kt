package com.example.cinemaspringapp.stub

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.contentType
import com.xebialabs.restito.semantics.Action.ok
import com.xebialabs.restito.semantics.Action.status
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.get
import com.xebialabs.restito.semantics.Condition.parameter
import com.xebialabs.restito.server.StubServer
import org.glassfish.grizzly.http.util.HttpStatus.INTERNAL_SERVER_ERROR_500

class OmdbApiStub(private val stubServer: StubServer) {

    fun stubOmdbApiOkResponse(imdbId: String) {
        omdbGetMovieDetailsStub(imdbId).then(
            ok(),
            contentType("application/json"),
            stringContent(omdbMovieDetailsResponse(imdbId))
        )
    }

    fun stubOmdbApiErrorResponse(imdbId: String) {
        omdbGetMovieDetailsStub(imdbId).then(
            ok(),
            contentType("application/json"),
            stringContent(omdbApiErrorResponse)
        )
    }

    fun stubOmdbApi500Response(imdbId: String) {
        omdbGetMovieDetailsStub(imdbId).then(
            status(INTERNAL_SERVER_ERROR_500),
            contentType("application/json"),
        )
    }

    private fun omdbGetMovieDetailsStub(imdbId: String) =
        whenHttp(stubServer).match(
            get("/"),
            parameter("apikey", OMDB_SECRET),
            parameter("i", imdbId)
        )
}

private fun omdbMovieDetailsResponse(imdbId: String) = """
    {
        "Title": "$TITLE",
        "Year": "2015",
        "Rated": "PG-13",
        "Released": "$RELEASED",
        "Runtime": "$RUNTIME",
        "Genre": "Action, Adventure, Thriller",
        "Director": "James Wan",
        "Writer": "Chris Morgan, Gary Scott Thompson",
        "Actors": "Vin Diesel, Paul Walker, Dwayne Johnson",
        "Plot": "$PLOT",
        "Language": "English, Thai, Arabic, Spanish",
        "Country": "United States, China, Japan, Canada, United Arab Emirates",
        "Awards": "36 wins & 36 nominations",
        "Poster": "https://m.media-amazon.com/images/M/MV5BMTQxOTA2NDUzOV5BMl5BanBnXkFtZTgwNzY2MTMxMzE@._V1_SX300.jpg",
        "Ratings": [
            {
                "Source": "Internet Movie Database",
                "Value": "7.1/10"
            },
            {
                "Source": "Rotten Tomatoes",
                "Value": "82%"
            },
            {
                "Source": "Metacritic",
                "Value": "67/100"
            }
        ],
        "Metascore": "67",
        "imdbRating": "$IMDB_RATING",
        "imdbVotes": "375,742",
        "imdbID": "$imdbId",
        "Type": "movie",
        "DVD": "15 Sep 2015",
        "BoxOffice": "${'$'}353,007,020",
        "Production": "N/A",
        "Website": "N/A",
        "Response": "True"
    }
""".trimIndent()

private val omdbApiErrorResponse = """
    {
      "Response": "False",
      "Error": "Error getting data."
    }
""".trimIndent()

private const val OMDB_SECRET = "omdb-so-secret"

const val TITLE = "Fast & Furious 7"
const val PLOT = "Deckard Shaw seeks revenge against Dominic Toretto and his family for his comatose brother."
const val RELEASED = "03 Apr 2015"
const val RUNTIME = "137 min"
const val IMDB_RATING = "7.1"
