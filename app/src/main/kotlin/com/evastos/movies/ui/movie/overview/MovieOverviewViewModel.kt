package com.evastos.movies.ui.movie.overview

import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.PagedList
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.model.Listing
import com.evastos.movies.domain.model.LoadingState
import com.evastos.movies.domain.repository.Repositories
import com.evastos.movies.ui.movie.base.BaseViewModel
import javax.inject.Inject

class MovieOverviewViewModel
@Inject constructor(
    private val movieOverviewRepository: Repositories.MovieOverviewRepository
) : BaseViewModel() {

    val moviesLiveData: MediatorLiveData<PagedList<Movie>> = MediatorLiveData()
    val loadingState: MediatorLiveData<LoadingState> = MediatorLiveData()

    private val nowPlayingMovieListing: Listing<Movie> =
            movieOverviewRepository.getNowPlayingMovies(disposables)
    private var searchMoviesListing: Listing<Movie>? = null

    private var retryGetMovies: () -> Unit = nowPlayingMovieListing.retry

    init {
        moviesLiveData.addSource(nowPlayingMovieListing.pagedList) {
            moviesLiveData.value = it
        }
        loadingState.addSource(nowPlayingMovieListing.loadingState) {
            loadingState.value = it
        }
    }

    fun retryGetMovies() {
        retryGetMovies.invoke()
    }

    fun refreshNowPlayingMovies() {
        searchMoviesListing?.let { listing ->
            moviesLiveData.removeSource(listing.pagedList)
            loadingState.removeSource(listing.loadingState)
            moviesLiveData.addSource(nowPlayingMovieListing.pagedList) {
                moviesLiveData.value = it
            }
            loadingState.addSource(nowPlayingMovieListing.loadingState) {
                loadingState.value = it
            }
            retryGetMovies = nowPlayingMovieListing.retry
            searchMoviesListing = null
        }
        nowPlayingMovieListing.refresh.invoke()
    }

    fun searchMovies(movieQuery: String) {
        searchMoviesListing = movieOverviewRepository.searchMovies(movieQuery, disposables)
        searchMoviesListing?.let { listing ->
            moviesLiveData.removeSource(nowPlayingMovieListing.pagedList)
            moviesLiveData.addSource(listing.pagedList) {
                moviesLiveData.value = it
            }
            loadingState.removeSource(nowPlayingMovieListing.loadingState)
            loadingState.addSource(listing.loadingState) {
                loadingState.value = it
            }
            retryGetMovies = listing.retry
        }
    }

    fun searchMovieSuggestions(movieQuery: String) {

    }
}
