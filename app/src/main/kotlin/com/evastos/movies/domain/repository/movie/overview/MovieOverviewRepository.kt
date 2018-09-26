package com.evastos.movies.domain.repository.movie.overview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.support.annotation.MainThread
import com.evastos.movies.data.encode.Encoder
import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.rx.applySchedulers
import com.evastos.movies.data.rx.mapException
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.datasource.movie.nowplaying.NowPlayingMoviesDataSourceFactory
import com.evastos.movies.domain.datasource.movie.search.SearchMoviesDataSourceFactory
import com.evastos.movies.domain.model.Listing
import com.evastos.movies.domain.repository.Repositories
import com.evastos.movies.ui.util.exception.ExceptionMessageProviders
import com.evastos.movies.ui.util.region.RegionProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MovieOverviewRepository
@Inject constructor(
    private val movieDbService: MovieDbService,
    private val exceptionMapper: ExceptionMappers.MovieDb,
    private val exceptionMessageProvider: ExceptionMessageProviders.MovieDb,
    private val encoder: Encoder,
    private val regionProvider: RegionProvider
) : Repositories.MovieOverviewRepository {

    companion object {
        private const val PAGE_SIZE = 4
    }

    @MainThread
    override fun getNowPlayingMovies(disposables: CompositeDisposable): Listing<Movie> {
        val sourceFactory = NowPlayingMoviesDataSourceFactory(
            movieDbService,
            exceptionMapper,
            exceptionMessageProvider,
            regionProvider,
            disposables
        )

        val pagedList = LivePagedListBuilder(sourceFactory, PAGE_SIZE).build()
        val loadingState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.loadingState
        }

        return Listing(
            pagedList = pagedList,
            loadingState = loadingState,
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            }
        )
    }

    @MainThread
    override fun searchMovies(query: String, disposables: CompositeDisposable): Listing<Movie> {
        val sourceFactory = SearchMoviesDataSourceFactory(
            encoder.encodeUrlQuery(query),
            movieDbService,
            exceptionMapper,
            exceptionMessageProvider,
            regionProvider,
            disposables
        )

        return Listing(
            pagedList = LivePagedListBuilder(sourceFactory, PAGE_SIZE).build(),
            loadingState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.loadingState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            }
        )
    }

    @MainThread
    override fun getMovieSuggestions(
        query: String,
        disposables: CompositeDisposable
    ): LiveData<List<Movie>> {
        val suggestionsLiveData = MutableLiveData<List<Movie>>()
        disposables.clear()
        disposables.add(
            movieDbService.searchMovies(
                query = encoder.encodeUrlQuery(query),
                region = regionProvider.getSystemRegion()
            )
                    .applySchedulers()
                    .mapException(exceptionMapper)
                    .subscribe({ response ->
                        val movies = response.results ?: emptyList()
                        suggestionsLiveData.postValue(movies)
                    }, {
                        suggestionsLiveData.postValue(null)
                    }
                    )
        )
        return suggestionsLiveData
    }
}
