package com.evastos.movies.domain.movie.overview

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.support.annotation.MainThread
import com.evastos.movies.data.encode.Encoder
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.model.moviedb.nowplaying.NowPlayingMoviesResponse
import com.evastos.movies.data.model.moviedb.search.SearchMoviesResponse
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.Repositories
import com.evastos.movies.domain.movie.overview.datasource.model.Listing
import com.evastos.movies.domain.movie.overview.datasource.MovieOverviewDataSourceFactory
import com.evastos.movies.ui.util.region.RegionProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MovieOverviewRepository
@Inject constructor(
    private val movieDbService: MovieDbService,
    private val encoder: Encoder,
    private val regionProvider: RegionProvider
) : Repositories.MovieOverviewRepository {

    @MainThread
    override fun getNowPlayingMovies(disposables: CompositeDisposable): Listing<Movie> {
        val sourceFactory = MovieOverviewDataSourceFactory(movieDbService, regionProvider, disposables)
        val livePagedList = LivePagedListBuilder(sourceFactory, 10)
                .build()
        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

    override fun searchMovies(query: String, page: Int?): Single<SearchMoviesResponse> {
        return movieDbService.searchMovies(
            query = encoder.encodeUrlQuery(query),
            page = page,
            region = regionProvider.getSystemRegion()
        )
    }
}
