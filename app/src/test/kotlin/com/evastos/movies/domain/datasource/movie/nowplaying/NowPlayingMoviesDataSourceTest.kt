package com.evastos.movies.domain.datasource.movie.nowplaying

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.evastos.movies.TestUtil
import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.exception.ExceptionMessageProviders
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NowPlayingMoviesDataSourceTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val movieDbService = mock<MovieDbService>()
    private val exceptionMapper = mock<ExceptionMappers.MovieDb>()
    private val exceptionMessageProvider = mock<ExceptionMessageProviders.MovieDb>()
    private val disposables = CompositeDisposable()

    private lateinit var nowPlayingMoviesDataSource: NowPlayingMoviesDataSource

    @Before
    fun setUp() {
        nowPlayingMoviesDataSource = NowPlayingMoviesDataSource(
            movieDbService = movieDbService,
            exceptionMapper = exceptionMapper,
            exceptionMessageProvider = exceptionMessageProvider,
            regionProvider = TestUtil.TestRegionProvider(),
            disposables = disposables
        )
    }

    @Test
    fun getLoadingState() {
    }

    @Test
    fun retryAllFailed() {
    }

    @Test
    fun loadBefore() {
    }

    @Test
    fun loadAfter() {
    }

    @Test
    fun loadInitial() {
    }
}