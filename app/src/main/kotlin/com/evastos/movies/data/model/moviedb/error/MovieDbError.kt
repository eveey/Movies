package com.evastos.movies.data.model.moviedb.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
In case of an HTTP error codes 401 and 404, The Movie DB returns error body with the status message.
 **/
@JsonClass(generateAdapter = true)
data class MovieDbError(
    @Json(name = "status_message") val statusMessage: String?,
    @Json(name = "status_code") val statusCode: Int?
)
