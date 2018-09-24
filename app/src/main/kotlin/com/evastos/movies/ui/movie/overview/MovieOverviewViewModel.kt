package com.evastos.movies.ui.movie.overview

import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.model.Listing
import com.evastos.movies.domain.repository.Repositories
import com.evastos.movies.ui.movie.base.BaseViewModel
import javax.inject.Inject

class MovieOverviewViewModel
@Inject constructor(
    movieOverviewRepository: Repositories.MovieOverviewRepository
) : BaseViewModel() {

    private val movieListing: Listing<Movie> =
            movieOverviewRepository.getNowPlayingMovies(disposables)
    val movies = movieListing.pagedList
    val loadingState = movieListing.loadingState

    fun retry() {
        movieListing.retry.invoke()
    }
}
