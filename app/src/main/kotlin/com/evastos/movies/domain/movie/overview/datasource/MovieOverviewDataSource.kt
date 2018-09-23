package com.evastos.movies.domain.movie.overview.datasource

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.rx.applySchedulers
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.movie.overview.datasource.model.LoadingState
import com.evastos.movies.ui.util.region.RegionProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * A data source that uses the before/after keys returned in page requests.
 */
class MovieOverviewDataSource(
    private val movieDbService: MovieDbService,
    private val regionProvider: RegionProvider,
    private val disposables: CompositeDisposable) : PageKeyedDataSource<Int, Movie>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<LoadingState>()

    val initialLoad = MutableLiveData<LoadingState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(LoadingState.LOADING)
        disposables.add(
            movieDbService.getNowPlaying(
                page = params.key,
                region = regionProvider.getSystemRegion()
            )
                    .applySchedulers()
                    .subscribe({ response ->
                        val nextPage = response.page?.plus(1)
                        val totalPages = response.totalPages

                        val nextPageToSearch =
                                if (nextPage != null && totalPages != null && nextPage <= totalPages) {
                                    nextPage
                                } else {
                                    null
                                }

                        val movies = response.results ?: emptyList()

                        retry = null
                        networkState.postValue(LoadingState.LOADED)
                        initialLoad.postValue(LoadingState.LOADED)
                        callback.onResult(movies, nextPageToSearch)

                    }, {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(LoadingState.ERROR)
                    }))

    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>) {
        networkState.postValue(LoadingState.LOADING)
        initialLoad.postValue(LoadingState.LOADING)

        disposables.add(
            movieDbService.getNowPlaying(page = 1, region = regionProvider.getSystemRegion())
                    .applySchedulers()
                    .subscribe({ response ->
                        val nextPage = response.page?.plus(1)
                        val totalPages = response.totalPages

                        val nextPageToSearch =
                                if (nextPage != null && totalPages != null && nextPage <= totalPages) {
                                    nextPage
                                } else {
                                    null
                                }

                        val movies = response.results ?: emptyList()

                        retry = null
                        networkState.postValue(LoadingState.LOADED)
                        initialLoad.postValue(LoadingState.LOADED)
                        callback.onResult(movies, response?.page?.minus(1), nextPageToSearch)

                    }, {
                        retry = {
                            loadInitial(params, callback)
                        }
                        networkState.postValue(LoadingState.ERROR)
                        initialLoad.postValue(LoadingState.ERROR)
                    }))
    }
}