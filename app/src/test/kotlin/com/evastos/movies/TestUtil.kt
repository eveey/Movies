package com.evastos.movies

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.data.model.moviedb.nowplaying.NowPlayingMoviesResponse
import com.evastos.movies.data.model.moviedb.search.SearchMoviesResponse
import com.evastos.movies.domain.model.Listing
import com.evastos.movies.domain.model.LoadingState
import com.evastos.movies.ui.util.region.RegionProvider
import com.nhaarman.mockito_kotlin.mock

class TestUtil {

    val nowPlayingMovies = MutableLiveData<PagedList<Movie>>()
    val nowPlayingLoadingState = MutableLiveData<LoadingState>()
    val nowPlayingMoviesRefresh = mock<() -> Unit>()
    val nowPlayingMoviesRetry = mock<() -> Unit>()

    val nowPlayingMoviesListing = Listing(
        pagedList = nowPlayingMovies,
        loadingState = nowPlayingLoadingState,
        refresh = nowPlayingMoviesRefresh,
        retry = nowPlayingMoviesRetry)

    val searchMovies1 = MutableLiveData<PagedList<Movie>>()
    val searchLoadingState1 = MutableLiveData<LoadingState>()
    val searchMoviesRefresh1 = mock<() -> Unit>()
    val searchMoviesRetry1 = mock<() -> Unit>()

    val searchMoviesListing1 = Listing(
        pagedList = searchMovies1,
        loadingState = searchLoadingState1,
        refresh = searchMoviesRefresh1,
        retry = searchMoviesRetry1)

    val searchMovies2 = MutableLiveData<PagedList<Movie>>()
    val searchLoadingState2 = MutableLiveData<LoadingState>()
    val searchMoviesRefresh2 = mock<() -> Unit>()
    val searchMoviesRetry2 = mock<() -> Unit>()

    val searchMoviesListing2 = Listing(
        pagedList = searchMovies2,
        loadingState = searchLoadingState2,
        refresh = searchMoviesRefresh2,
        retry = searchMoviesRetry2)

    val movieSuggestions1 = MutableLiveData<List<Movie>>()
    val movieSuggestions2 = MutableLiveData<List<Movie>>()

    val movie = Movie(
        posterPath = "posterPath",
        isAdult = false,
        overview = "overview",
        releaseDate = "releaseDate",
        genreIds = emptyList(),
        id = 1234,
        originalTitle = "originalTitle",
        originalLanguage = "originalLanguage",
        title = "title",
        backdropPath = "backdropPath",
        popularity = 7.2324,
        voteCount = 4323,
        isVideo = false,
        voteAverage = 7.5
    )

    val moviesList1 = listOf(movie)
    val moviesList2 = listOf(movie, movie)

    val nowPlayingMoviesList = mock<PagedList<Movie>>()
    val searchedMoviesList1 = mock<PagedList<Movie>>()
    val searchedMoviesList2 = mock<PagedList<Movie>>()

    val loading = LoadingState.Loading()
    val success = LoadingState.Success()
    val error = LoadingState.Error("errorMsg")

    val searchMoviesResponse = SearchMoviesResponse(
        page = 1,
        results = moviesList2,
        totalPages = 2,
        totalResults = 2
    )

    val nowPlayingMoviesResponse = NowPlayingMoviesResponse(
        page = 1,
        results = moviesList2,
        dates = null,
        totalPages = 2,
        totalResults = 2
    )

    internal class TestRegionProvider : RegionProvider() {
        override fun getSystemRegion(): String? {
            return "US"
        }
    }
}
