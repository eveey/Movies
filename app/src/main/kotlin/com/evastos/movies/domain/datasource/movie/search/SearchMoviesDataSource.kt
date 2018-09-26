package com.evastos.movies.domain.datasource.movie.search

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.model.moviedb.search.SearchMoviesResponse
import com.evastos.movies.data.rx.applySchedulers
import com.evastos.movies.data.rx.mapException
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.model.LoadingState
import com.evastos.movies.ui.util.exception.ExceptionMessageProviders
import com.evastos.movies.ui.util.region.RegionProvider
import io.reactivex.disposables.CompositeDisposable

class SearchMoviesDataSource(
    private val query: String,
    private val movieDbService: MovieDbService,
    private val exceptionMapper: ExceptionMappers.MovieDb,
    private val exceptionMessageProvider: ExceptionMessageProviders.MovieDb,
    private val regionProvider: RegionProvider,
    private val disposables: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    companion object {
        private const val PAGE_INITIAL = 1
    }

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    val loadingState = MutableLiveData<LoadingState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie>
    ) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        loadingState.postValue(LoadingState.Loading())
        disposables.add(
            movieDbService.searchMovies(
                query = query,
                page = params.key,
                region = regionProvider.getSystemRegion()
            )
                    .applySchedulers()
                    .mapException(exceptionMapper)
                    .subscribe({ response ->
                        val movies = response.results ?: emptyList()
                        retry = null
                        loadingState.postValue(LoadingState.LoadingSuccess())
                        callback.onResult(movies, getNextPage(response))
                    }, {
                        retry = {
                            loadAfter(params, callback)
                        }
                        loadingState.postValue(
                            LoadingState.LoadingError(exceptionMessageProvider.getMessage(it)))
                    }
                    )
        )
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        loadingState.postValue(LoadingState.Loading())
        disposables.add(
            movieDbService.searchMovies(
                query = query,
                page = PAGE_INITIAL,
                region = regionProvider.getSystemRegion()
            )
                    .applySchedulers()
                    .mapException(exceptionMapper)
                    .subscribe({ response ->
                        val nextPage = getNextPage(response)
                        val previousPage = response?.page?.minus(1)
                        val movies = response.results ?: emptyList()

                        retry = null
                        loadingState.postValue(LoadingState.LoadingSuccess())
                        callback.onResult(movies, previousPage, nextPage)
                    }, {
                        retry = {
                            loadInitial(params, callback)
                        }
                        val errorMessage = exceptionMessageProvider.getMessage(it)
                        loadingState.postValue(LoadingState.LoadingError(errorMessage))
                    })
        )
    }

    private fun getNextPage(response: SearchMoviesResponse): Int? {
        val nextPageVal = response.page?.plus(1)
        val totalPagesVal = response.totalPages
        return if (nextPageVal != null && totalPagesVal != null && nextPageVal <= totalPagesVal) {
            nextPageVal
        } else {
            null
        }
    }
}
