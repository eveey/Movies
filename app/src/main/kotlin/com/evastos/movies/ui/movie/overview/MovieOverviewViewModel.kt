package com.evastos.movies.ui.movie.overview

import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.PagedList
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.model.Listing
import com.evastos.movies.domain.model.LoadingState
import com.evastos.movies.domain.repository.Repositories
import com.evastos.movies.ui.livedata.SingleLiveEvent
import com.evastos.movies.ui.movie.base.BaseViewModel
import javax.inject.Inject

class MovieOverviewViewModel
@Inject constructor(
    private val movieOverviewRepository: Repositories.MovieOverviewRepository
) : BaseViewModel() {

    val moviesLiveData = MediatorLiveData<PagedList<Movie>>()
    val loadingStateLiveData = MediatorLiveData<LoadingState>()
    val movieDetailsLiveData = SingleLiveEvent<Movie>()

    private val nowPlayingMovieListing: Listing<Movie> =
            movieOverviewRepository.getNowPlayingMovies(disposables)
    private var searchMoviesListing: Listing<Movie>? = null

    private var retryGetMovies: () -> Unit = nowPlayingMovieListing.retry

    init {
        moviesLiveData.addSource(nowPlayingMovieListing.pagedList) {
            moviesLiveData.value = it
        }
        loadingStateLiveData.addSource(nowPlayingMovieListing.loadingState) {
            loadingStateLiveData.value = it
        }
    }

    fun onRetry() {
        retryGetMovies.invoke()
    }

    fun onRefresh() {
        searchMoviesListing?.let { listing ->
            moviesLiveData.removeSource(listing.pagedList)
            loadingStateLiveData.removeSource(listing.loadingState)
            moviesLiveData.addSource(nowPlayingMovieListing.pagedList) {
                moviesLiveData.value = it
            }
            loadingStateLiveData.addSource(nowPlayingMovieListing.loadingState) {
                loadingStateLiveData.value = it
            }
            retryGetMovies = nowPlayingMovieListing.retry
            searchMoviesListing = null
        }
        nowPlayingMovieListing.refresh.invoke()
    }

    fun onSearchQuerySubmit(movieQuery: String) {
        searchMoviesListing = movieOverviewRepository.searchMovies(movieQuery, disposables)
        searchMoviesListing?.let { listing ->
            moviesLiveData.removeSource(nowPlayingMovieListing.pagedList)
            moviesLiveData.addSource(listing.pagedList) {
                moviesLiveData.value = it
            }
            loadingStateLiveData.removeSource(nowPlayingMovieListing.loadingState)
            loadingStateLiveData.addSource(listing.loadingState) {
                loadingStateLiveData.value = it
            }
            retryGetMovies = listing.retry
        }
    }

    fun onSearchQueryChange(movieQuery: String) {
    }

    fun onMovieClick(movie: Movie?) {
        movieDetailsLiveData.postValue(movie)
    }
}
