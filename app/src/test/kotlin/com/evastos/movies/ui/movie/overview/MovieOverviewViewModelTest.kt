package com.evastos.movies.ui.movie.overview

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.evastos.movies.RxImmediateSchedulerRule
import com.evastos.movies.domain.repository.Repositories
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieOverviewViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository = mock<Repositories.MovieOverviewRepository>()

    private lateinit var viewModel: MovieOverviewViewModel

    @Before
    fun setUp() {
        viewModel = MovieOverviewViewModel(repository)
    }

    @Test
    fun getMoviesLiveData() {
    }

    @Test
    fun getLoadingStateLiveData() {
    }

    @Test
    fun getMovieSuggestionsLiveData() {
    }

    @Test
    fun getMovieDetailsLiveData() {
    }

    @Test
    fun onRetry() {
    }

    @Test
    fun onRefresh() {
    }

    @Test
    fun onSearchQuerySubmit() {
    }

    @Test
    fun onSearchQueryChange() {
    }

    @Test
    fun onMovieClick() {
    }
}