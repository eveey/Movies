package com.evastos.movies.ui.movie.overview

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import com.evastos.movies.TestUtil
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.model.LoadingState
import com.evastos.movies.domain.repository.Repositories
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.check
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieOverviewViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository = mock<Repositories.MovieOverviewRepository>()

    private val moviesLiveDataObserver = mock<Observer<PagedList<Movie>>>()
    private val loadingStateLiveDataObserver = mock<Observer<LoadingState>>()
    private val movieSuggestionsLiveDataObserver = mock<Observer<List<Movie>>>()
    private val movieDetailsLiveDataObserver = mock<Observer<Movie>>()

    private lateinit var viewModel: MovieOverviewViewModel

    private val util = TestUtil()

    @Before
    fun setUp() {
        whenever(repository.getNowPlayingMovies(any()))
                .thenReturn(util.nowPlayingMoviesListing)
        whenever(repository.searchMovies(eq("Melancholia"), any()))
                .thenReturn(util.searchMoviesListing1)
        whenever(repository.searchMovies(eq("Interstellar"), any()))
                .thenReturn(util.searchMoviesListing2)
        whenever(repository.getMovieSuggestions(eq("Not"), any()))
                .thenReturn(util.movieSuggestions1)
        whenever(repository.getMovieSuggestions(eq("Nothing"), any()))
                .thenReturn(util.movieSuggestions2)

        viewModel = MovieOverviewViewModel(repository)
        viewModel.moviesLiveData.observeForever(moviesLiveDataObserver)
        viewModel.loadingStateLiveData.observeForever(loadingStateLiveDataObserver)
        viewModel.movieSuggestionsLiveData.observeForever(movieSuggestionsLiveDataObserver)
        viewModel.movieDetailsLiveData.observeForever(movieDetailsLiveDataObserver)
    }

    @Test
    fun init_withNowPlayingMoviesData_postsNowPlayingMoviesData() {
        initialLoad()

        verify(moviesLiveDataObserver).onChanged(util.nowPlayingMoviesList)
        verify(loadingStateLiveDataObserver).onChanged(util.loading)
        verify(loadingStateLiveDataObserver).onChanged(util.success)
    }

    @Test
    fun init_withNowPlayingMoviesData_doesNotPostSuggestionsAndMovieDetails() {
        initialLoad()

        verify(movieSuggestionsLiveDataObserver, never()).onChanged(any())
        verify(movieDetailsLiveDataObserver, never()).onChanged(any())
    }

    @Test
    fun onRetry_withNowPlayingMovies_retriesNowPlayingMovies() {
        initialLoad()

        viewModel.onRetry()

        verify(util.nowPlayingMoviesRetry).invoke()
        verify(util.searchMoviesRetry1, never()).invoke()
        verify(util.searchMoviesRetry2, never()).invoke()
    }

    @Test
    fun onRetry_withSearchedMovies_retriesSearchMovies() {
        initialLoad()
        viewModel.onSearchQuerySubmit("Melancholia")

        viewModel.onRetry()

        verify(util.searchMoviesRetry1).invoke()
        verify(util.nowPlayingMoviesRetry, never()).invoke()
    }

    @Test
    fun onRefresh_withNowPlayingMovies_retriesNowPlayingMovie() {
        initialLoad()

        viewModel.onRefresh()

        verify(util.nowPlayingMoviesRefresh).invoke()
        verify(util.searchMoviesRefresh1, never()).invoke()
        verify(util.searchMoviesRefresh2, never()).invoke()
    }

    @Test
    fun onRefresh_withSearchedMovies_retriesNowPlayingMovie() {
        initialLoad()
        viewModel.onSearchQuerySubmit("Melancholia")

        viewModel.onRefresh()

        verify(util.nowPlayingMoviesRefresh).invoke()
        verify(util.searchMoviesRefresh1, never()).invoke()
        verify(util.searchMoviesRefresh2, never()).invoke()
    }

    @Test
    fun onSearchQuerySubmit_postsSearchedMoviesList() {
        initialLoad()

        viewModel.onSearchQuerySubmit("Melancholia")

        verify(moviesLiveDataObserver).onChanged(util.searchedMoviesList1)
        verify(loadingStateLiveDataObserver).onChanged(util.loading)
        verify(loadingStateLiveDataObserver).onChanged(util.error)
    }

    @Test
    fun onSearchQuerySubmitTwice_postsNextSearchedMoviesList() {
        initialLoad()

        viewModel.onSearchQuerySubmit("Melancholia")
        viewModel.onSearchQuerySubmit("Interstellar")

        verify(moviesLiveDataObserver).onChanged(util.searchedMoviesList1)
        verify(moviesLiveDataObserver).onChanged(util.searchedMoviesList2)
    }

    @Test
    fun onSearchQuerySubmit_doesNotPostSuggestionsAndMovieDetails() {
        initialLoad()

        viewModel.onSearchQuerySubmit("Melancholia")

        verify(movieSuggestionsLiveDataObserver, never()).onChanged(any())
        verify(movieDetailsLiveDataObserver, never()).onChanged(any())
    }

    @Test
    fun onSearchQueryChange_postsMovieSuggestions() {
        initialLoad()

        viewModel.onSearchQueryChange("Not")

        verify(movieSuggestionsLiveDataObserver).onChanged(check { suggestions ->
            assertEquals(1, suggestions.size)
            assertEquals(util.movie, suggestions[0])
        })
    }

    @Test
    fun onSearchQueryChange_withSecondChange_postsNextMovieSuggestions() {
        initialLoad()

        viewModel.onSearchQueryChange("Not")
        viewModel.onSearchQueryChange("Nothing")

        verify(movieSuggestionsLiveDataObserver).onChanged(util.moviesList1)
        verify(movieSuggestionsLiveDataObserver).onChanged(util.moviesList2)
    }

    @Test
    fun onMovieClick() {
        initialLoad()

        viewModel.onMovieClick(util.movie)

        verify(movieDetailsLiveDataObserver).onChanged(util.movie)
    }

    private fun initialLoad() {
        util.nowPlayingMovies.value = util.nowPlayingMoviesList
        util.nowPlayingLoadingState.value = util.loading
        util.nowPlayingLoadingState.value = util.success
        util.searchMovies1.value = util.searchedMoviesList1
        util.searchLoadingState1.value = util.loading
        util.searchLoadingState1.value = util.error
        util.searchMovies2.value = util.searchedMoviesList2
        util.searchLoadingState2.value = util.loading
        util.searchLoadingState2.value = util.success
        util.movieSuggestions1.value = util.moviesList1
        util.movieSuggestions2.value = util.moviesList2
    }
}
