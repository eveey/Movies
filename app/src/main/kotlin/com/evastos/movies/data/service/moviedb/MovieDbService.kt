package com.evastos.movies.data.service.moviedb

import com.evastos.movies.BuildConfig.API_KEY
import com.evastos.movies.data.model.moviedb.nowplaying.NowPlayingMoviesResponse
import com.evastos.movies.data.model.moviedb.search.SearchMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDbService {

    @GET("movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Single<NowPlayingMoviesResponse>

    @GET("search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Single<SearchMoviesResponse>
}
