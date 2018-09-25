package com.evastos.movies.data.model.moviedb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Dates(
    @Json(name = "maximum") val maximumDate: String?,
    @Json(name = "minimum") val minimumDate: String?
)
