package com.evastos.movies.domain.datasource.movie.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.paging.PageKeyedDataSource
import com.evastos.movies.RxImmediateSchedulerRule
import com.evastos.movies.TestUtil
import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.exception.moviedb.MovieDbException
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.exception.ExceptionMessageProviders
import com.evastos.movies.domain.model.LoadingState
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.check
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchMoviesDataSourceTest {

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
    private val query = "Interstellar"
    private val loadingStateObserver = mock<Observer<LoadingState>>()

    private lateinit var searchMoviesDataSource: SearchMoviesDataSource

    private val loadParams = mock<PageKeyedDataSource.LoadParams<Int>>()
    private val loadCallback = mock<PageKeyedDataSource.LoadCallback<Int, Movie>>()
    private val loadInitialParams = mock<PageKeyedDataSource.LoadInitialParams<Int>>()
    private val loadInitialCallback = mock<PageKeyedDataSource.LoadInitialCallback<Int, Movie>>()
    private val util = TestUtil()

    @Before
    fun setUp() {
        whenever(exceptionMapper.map(any())).thenReturn(MovieDbException.UnknownException())
        whenever(exceptionMessageProvider.getMessage(any()))
                .thenReturn("errorMsg")
        whenever(exceptionMessageProvider.getMessage(any<Throwable>())).thenReturn("errorMsg")


        searchMoviesDataSource = SearchMoviesDataSource(
            query = query,
            movieDbService = movieDbService,
            exceptionMapper = exceptionMapper,
            exceptionMessageProvider = exceptionMessageProvider,
            regionProvider = TestUtil.TestRegionProvider(),
            disposables = disposables
        )
        searchMoviesDataSource.loadingState.observeForever(loadingStateObserver)
    }

    @Test
    fun loadBefore_doesNothing() {
        searchMoviesDataSource.loadBefore(loadParams, loadCallback)

        verifyNoMoreInteractions(movieDbService)
        verifyNoMoreInteractions(loadingStateObserver)
    }

    @Test
    fun loadAfter_withSuccessResponse_callsOnResult() {
        whenever(movieDbService.searchMovies(any(), any(), eq(null), any()))
                .thenReturn(Single.just(util.searchMoviesResponse))

        searchMoviesDataSource.loadAfter(loadParams, loadCallback)

        verify(loadingStateObserver, times(2)).onChanged(check {
            assertNotNull(it)
        })
        verify(loadCallback).onResult(util.moviesList2, 2)
    }

    @Test
    fun loadAfter_withErrorResponse_doesNotCallOnResult() {
        whenever(movieDbService.searchMovies(any(), any(), eq(null), any()))
                .thenReturn(Single.error(Throwable()))

        searchMoviesDataSource.loadAfter(loadParams, loadCallback)

        verify(loadingStateObserver, times(2)).onChanged(check {
            assertNotNull(it)
        })
        verify(loadCallback, never()).onResult(any(), any())
    }

    @Test
    fun loadInitial_withSuccessResponse_callsOnResult() {
        whenever(movieDbService.searchMovies(any(), any(), eq(1), any()))
                .thenReturn(Single.just(util.searchMoviesResponse))

        searchMoviesDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        verify(loadingStateObserver, times(2)).onChanged(check {
            assertNotNull(it)
        })
        verify(loadInitialCallback).onResult(util.moviesList2, 0, 2)
    }

    @Test
    fun loadInitial_withErrorResponse_doesNotCallOnResult() {
        whenever(movieDbService.searchMovies(any(), any(), eq(1), any()))
                .thenReturn(Single.error(Throwable()))

        searchMoviesDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        verify(loadingStateObserver, times(2)).onChanged(check {
            assertNotNull(it)
        })
        verify(loadInitialCallback, never()).onResult(any(), any(), any())
    }

    @Test
    fun retryAllFailed_withLoadAfter_withSuccessResponse_doesNotRetry() {
        whenever(movieDbService.searchMovies(any(), any(), eq(null), any()))
                .thenReturn(Single.just(util.searchMoviesResponse))
        searchMoviesDataSource.loadAfter(loadParams, loadCallback)

        searchMoviesDataSource.retryAllFailed()

        verify(loadingStateObserver, times(2)).onChanged(check {
            assertNotNull(it)
        })
        verify(loadCallback).onResult(util.moviesList2, 2)
        verify(movieDbService).searchMovies(any(), any(), eq(null), any())
    }

    @Test
    fun retryAllFailed_withLoadAfter_withErrorResponse_doesRetry() {
        whenever(movieDbService.searchMovies(any(), any(), eq(null), any()))
                .thenReturn(Single.error(Throwable()))
        searchMoviesDataSource.loadAfter(loadParams, loadCallback)
        whenever(movieDbService.searchMovies(any(), any(), eq(null), any()))
                .thenReturn(Single.just(util.searchMoviesResponse))

        searchMoviesDataSource.retryAllFailed()

        verify(loadingStateObserver, times(4)).onChanged(check {
            assertNotNull(it)
        })
        verify(loadCallback).onResult(util.moviesList2, 2)
        verify(movieDbService, times(2)).searchMovies(any(), any(), eq(null), any())
    }

    @Test
    fun retryAllFailed_withLoadInitial_withSuccessResponse_doesNotRetry() {
        whenever(movieDbService.searchMovies(any(), any(), eq(1), any()))
                .thenReturn(Single.just(util.searchMoviesResponse))
        searchMoviesDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        searchMoviesDataSource.retryAllFailed()

        verify(loadingStateObserver, times(2)).onChanged(check {
            assertNotNull(it)
        })
        verify(loadInitialCallback).onResult(util.moviesList2, 0, 2)
        verify(movieDbService).searchMovies(any(), any(), eq(1), any())
    }

    @Test
    fun retryAllFailed_withLoadInitial_withErrorResponse_doesRetry() {
        whenever(movieDbService.searchMovies(any(), any(), eq(1), any()))
                .thenReturn(Single.error(Throwable()))
        searchMoviesDataSource.loadInitial(loadInitialParams, loadInitialCallback)
        whenever(movieDbService.searchMovies(any(), any(), eq(1), any()))
                .thenReturn(Single.just(util.searchMoviesResponse))

        searchMoviesDataSource.retryAllFailed()

        verify(loadingStateObserver, times(4)).onChanged(check {
            assertNotNull(it)
        })
        verify(loadInitialCallback).onResult(util.moviesList2, 0, 2)
        verify(movieDbService, times(2))
                .searchMovies(any(), any(), eq(1), any())
    }
}
