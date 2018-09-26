package com.evastos.movies.ui.movie.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.evastos.movies.TestUtil
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieDetailsViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val backdropPhotoLiveDataObserver = mock<Observer<String>>()
    private val movieTitleLiveDataObserver = mock<Observer<String>>()
    private val movieOverviewLiveDataObserver = mock<Observer<String>>()

    private lateinit var viewModel: MovieDetailsViewModel

    private val util = TestUtil()

    @Before
    fun setUp() {
        viewModel = MovieDetailsViewModel()
        viewModel.backdropPhotoLiveData.observeForever(backdropPhotoLiveDataObserver)
        viewModel.movieTitleLiveData.observeForever(movieTitleLiveDataObserver)
        viewModel.movieOverviewLiveData.observeForever(movieOverviewLiveDataObserver)
    }

    @Test
    fun onCreate_withMovie_postsMovieData() {
        val movie = util.movie

        viewModel.onCreate(movie)

        verify(backdropPhotoLiveDataObserver).onChanged(movie.backdropPath)
        verify(movieTitleLiveDataObserver).onChanged(movie.title)
        verify(movieOverviewLiveDataObserver).onChanged(movie.overview)
    }
}
