package com.evastos.movies.domain

import com.evastos.movies.data.model.moviedb.nowplaying.NowPlayingMoviesResponse
import com.evastos.movies.data.model.moviedb.search.SearchMoviesResponse
import io.reactivex.Single

interface Repositories {

    interface MovieOverviewRepository {

        fun getNowPlayingMovies(page: Int? = null): Single<NowPlayingMoviesResponse>

        fun searchMovies(query: String, page: Int? = null): Single<SearchMoviesResponse>
    }
}
