package com.evastos.movies.ui.movie.overview

import android.arch.lifecycle.MutableLiveData
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.Repositories
import com.evastos.movies.domain.movie.overview.datasource.model.Listing
import com.evastos.movies.ui.movie.base.BaseViewModel
import javax.inject.Inject

class MovieOverviewViewModel
@Inject constructor(
    private val movieOverviewRepository: Repositories.MovieOverviewRepository
) : BaseViewModel() {

    private val listing: Listing<Movie> = movieOverviewRepository.getNowPlayingMovies(disposables)
    val posts = listing.pagedList
    val networkState = listing.networkState
    val refreshState = listing.refreshState

    fun refresh() {
        listing.refresh.invoke()
    }

    fun retry() {
        listing.retry.invoke()
    }

}
