package com.evastos.movies.domain.movie.overview

import com.evastos.movies.data.encode.Encoder
import com.evastos.movies.data.model.moviedb.nowplaying.NowPlayingMoviesResponse
import com.evastos.movies.data.model.moviedb.search.SearchMoviesResponse
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.evastos.movies.domain.Repositories
import com.evastos.movies.ui.util.region.RegionProvider
import io.reactivex.Single
import javax.inject.Inject

class MovieOverviewRepository
@Inject constructor(
    private val movieDbService: MovieDbService,
    private val encoder: Encoder,
    private val regionProvider: RegionProvider
) : Repositories.MovieOverviewRepository {

    override fun getNowPlayingMovies(page: Int?): Single<NowPlayingMoviesResponse> {
        return movieDbService.getNowPlaying(
            page = page,
            region = regionProvider.getSystemRegion()
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
