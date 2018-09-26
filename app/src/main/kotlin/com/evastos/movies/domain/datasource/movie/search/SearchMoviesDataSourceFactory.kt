package com.evastos.movies.domain.datasource.movie.search

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.ui.util.exception.ExceptionMessageProviders
import com.evastos.movies.ui.util.region.RegionProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its loading request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
class SearchMoviesDataSourceFactory(
    private val query: String,
    private val movieDbService: MovieDbService,
    private val exceptionMapper: ExceptionMappers.MovieDb,
    private val exceptionMessageProvider: ExceptionMessageProviders.MovieDb,
    private val regionProvider: RegionProvider,
    private val disposables: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val sourceLiveData = MutableLiveData<SearchMoviesDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val source = SearchMoviesDataSource(
            query,
            movieDbService,
            exceptionMapper,
            exceptionMessageProvider,
            regionProvider,
            disposables
        )
        sourceLiveData.postValue(source)
        return source
    }
}
