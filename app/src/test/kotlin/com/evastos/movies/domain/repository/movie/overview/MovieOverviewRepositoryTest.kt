package com.evastos.movies.domain.repository.movie.overview

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.evastos.movies.RxImmediateSchedulerRule
import com.evastos.movies.TestUtil
import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.exception.moviedb.MovieDbException
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.exception.ExceptionMessageProviders
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.isNull
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieOverviewRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val movieDbService = mock<MovieDbService>()
    private val exceptionMapper = mock<ExceptionMappers.MovieDb>()
    private val exceptionMessageProvider = mock<ExceptionMessageProviders.MovieDb>()
    private val disposables = CompositeDisposable()
    private val movieSuggestionsLiveDataObserver = mock<Observer<List<Movie>>>()
    private val util = TestUtil()

    private lateinit var movieOverviewRepository: MovieOverviewRepository

    @Before
    fun setUp() {
        whenever(movieDbService.searchMovies(any(), any(), eq(null), any()))
                .thenReturn(Single.just(util.searchMoviesResponse))
        whenever(exceptionMapper.map(any())).thenReturn(MovieDbException.ClientException())

        movieOverviewRepository = MovieOverviewRepository(
            movieDbService = movieDbService,
            exceptionMapper = exceptionMapper,
            exceptionMessageProvider = exceptionMessageProvider,
            regionProvider = TestUtil.TestRegionProvider()
        )
    }

    @Test
    fun getNowPlayingMovies_returnsNowPlayingMoviesListing() {
        val listing = movieOverviewRepository.getNowPlayingMovies(disposables)

        assertNotNull(listing.pagedList)
        assertNotNull(listing.loadingState)
        assertNotNull(listing.refresh)
        assertNotNull(listing.retry)
    }

    @Test
    fun searchMovies_returnsSearchMoviesListing() {
        val listing = movieOverviewRepository.searchMovies("A24", disposables)

        assertNotNull(listing.pagedList)
        assertNotNull(listing.loadingState)
        assertNotNull(listing.refresh)
        assertNotNull(listing.retry)
    }

    @Test
    fun getMovieSuggestions_returnsMovieSuggestionsLiveData() {
        val movieSuggestionsLiveData =
                movieOverviewRepository.getMovieSuggestions("Heist", disposables)

        assertNotNull(movieSuggestionsLiveData)
    }

    @Test
    fun getMovieSuggestions_callsMovieDbService() {
        movieOverviewRepository.getMovieSuggestions("Heist", disposables)

        verify(movieDbService).searchMovies(any(), eq("Heist"), isNull(), eq("US"))
    }

    @Test
    fun getMovieSuggestions_withSuccess_postsMovieSuggestions() {
        movieOverviewRepository.getMovieSuggestions("Heist", disposables)
                .observeForever(movieSuggestionsLiveDataObserver)

        verify(movieSuggestionsLiveDataObserver).onChanged(util.moviesList2)
    }
}
