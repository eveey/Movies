package com.evastos.movies.domain.repository

import android.arch.lifecycle.LiveData
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.model.Listing
import io.reactivex.disposables.CompositeDisposable

interface Repositories {

    interface MovieOverviewRepository {

        fun getNowPlayingMovies(disposables: CompositeDisposable): Listing<Movie>

        fun searchMovies(query: String, disposables: CompositeDisposable): Listing<Movie>

        fun getMovieSuggestions(
            query: String,
            disposables: CompositeDisposable
        ): LiveData<List<Movie>>
    }
}
