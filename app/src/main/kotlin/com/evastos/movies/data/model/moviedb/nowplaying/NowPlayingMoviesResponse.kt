package com.evastos.movies.data.model.moviedb.nowplaying

import com.evastos.movies.data.model.moviedb.Dates
import com.evastos.movies.data.model.moviedb.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NowPlayingMoviesResponse(
    @Json(name = "page") val page: Int?,
    @Json(name = "results") val results: List<Movie>?,
    @Json(name = "dates") val dates: Dates?,
    @Json(name = "total_pages") val totalPages: Int?,
    @Json(name = "total_results") val totalResults: Int?
)
