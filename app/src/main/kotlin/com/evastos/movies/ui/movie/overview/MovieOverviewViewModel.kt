package com.evastos.movies.ui.movie.overview

import android.arch.lifecycle.LiveData
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
    private val repository: Repositories.MovieOverviewRepository
) : BaseViewModel() {

    // NOW PLAYING MOVIES
    val moviesLiveData = MediatorLiveData<PagedList<Movie>>()

    // LOADING STATE
    val loadingStateLiveData = MediatorLiveData<LoadingState>()

    // MOVIES SUGGESTIONS
    val movieSuggestionsLiveData = MediatorLiveData<List<Movie>>()

    // MOVIE DETAILS
    val movieDetailsLiveData = SingleLiveEvent<Movie>()

    private val nowPlayingMovieListing: Listing<Movie> =
            repository.getNowPlayingMovies(disposables)
    private var searchMoviesListing: Listing<Movie>? = null
    private var suggestionsLiveData: LiveData<List<Movie>>? = null

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
        movieSuggestionsLiveData.postValue(null)
        searchMoviesListing = repository.searchMovies(movieQuery, disposables)
                .apply {
                    moviesLiveData.removeSource(nowPlayingMovieListing.pagedList)
                    moviesLiveData.addSource(this.pagedList) {
                        moviesLiveData.value = it
                    }
                    loadingStateLiveData.removeSource(nowPlayingMovieListing.loadingState)
                    loadingStateLiveData.addSource(this.loadingState) {
                        loadingStateLiveData.value = it
                    }
                    retryGetMovies = this.retry
                }
    }

    fun onSearchQueryChange(movieQuery: String) {
        suggestionsLiveData?.let {
            movieSuggestionsLiveData.removeSource(it)
        }
        suggestionsLiveData = repository.getMovieSuggestions(movieQuery, disposables)
                .apply {
                    movieSuggestionsLiveData.addSource(this) {
                        movieSuggestionsLiveData.value = it
                    }
                }
    }

    fun onMovieClick(movie: Movie?) {
        movieDetailsLiveData.postValue(movie)
    }
}
