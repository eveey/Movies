package com.evastos.movies.data.model.moviedb

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "adult") val isAdult: Boolean?,
    @Json(name = "overview") val overview: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>?,
    @Json(name = "id") val id: Int?,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "original_language") val originalLanguage: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "popularity") val popularity: Double?,
    @Json(name = "vote_count") val voteCount: Int?,
    @Json(name = "video") val isVideo: Boolean?,
    @Json(name = "vote_average") val voteAverage: Double?
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true

        val movie = other as? Movie?
        return movie?.id === this.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }
}
