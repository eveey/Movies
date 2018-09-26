package com.evastos.movies.domain.datasource.movie.nowplaying

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.evastos.movies.TestUtil
import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.exception.ExceptionMessageProviders
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NowPlayingMoviesDataSourceFactoryTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val movieDbService = mock<MovieDbService>()
    private val exceptionMapper = mock<ExceptionMappers.MovieDb>()
    private val exceptionMessageProvider = mock<ExceptionMessageProviders.MovieDb>()
    private val disposables = CompositeDisposable()
    private val sourceLiveDataObserver = mock<Observer<NowPlayingMoviesDataSource>>()

    private lateinit var nowPlayingMoviesDataSourceFactory: NowPlayingMoviesDataSourceFactory

    @Before
    fun setUp() {
        nowPlayingMoviesDataSourceFactory = NowPlayingMoviesDataSourceFactory(
            movieDbService = movieDbService,
            exceptionMapper = exceptionMapper,
            exceptionMessageProvider = exceptionMessageProvider,
            regionProvider = TestUtil.TestRegionProvider(),
            disposables = disposables
        )
    }

    @Test
    fun create_postsNowPlayingMoviesDataSource() {
        nowPlayingMoviesDataSourceFactory.sourceLiveData.observeForever(sourceLiveDataObserver)

        val sourceDataFactory = nowPlayingMoviesDataSourceFactory.create()

        verify(sourceLiveDataObserver).onChanged(sourceDataFactory as NowPlayingMoviesDataSource)
    }
}
