package com.evastos.movies.domain

import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.model.moviedb.nowplaying.NowPlayingMoviesResponse
import com.evastos.movies.data.model.moviedb.search.SearchMoviesResponse
import com.evastos.movies.domain.movie.overview.datasource.model.Listing
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

interface Repositories {

    interface MovieOverviewRepository {

        fun searchMovies(query: String, page: Int? = null): Single<SearchMoviesResponse>

        fun getNowPlayingMovies(disposables: CompositeDisposable): Listing<Movie>
    }
}
