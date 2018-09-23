package com.evastos.movies.ui.movie.overview

import android.arch.lifecycle.MutableLiveData
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.rx.applySchedulers
import com.evastos.movies.domain.Repositories
import com.evastos.movies.ui.movie.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class MovieOverviewViewModel
@Inject constructor(
    private val movieOverviewRepository: Repositories.MovieOverviewRepository
) : BaseViewModel() {

    val moviesLiveData = MutableLiveData<List<Movie>>()

    init {
        disposables.add(movieOverviewRepository.getNowPlayingMovies()
                .applySchedulers()
                .subscribe { t1, t2 ->
                    Timber.i(t1.toString())

                    t1.results?.let {
                        if (moviesLiveData.value == null) {
                            moviesLiveData.postValue(it.toMutableList())
                        } else {
                            val moviesList = moviesLiveData.value!!.toMutableList()
                            moviesList.addAll(it)
                            moviesLiveData.postValue(moviesList.toMutableList())
                        }
                    }
                    Timber.e(t2)
                })

        disposables.add(movieOverviewRepository.searchMovies("mission impossible")
                .applySchedulers()
                .subscribe { t1, t2 ->
                    Timber.i(t1.toString())
                    Timber.e(t2)
                })
    }
}
