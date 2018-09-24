package com.evastos.movies.inject.module

import com.evastos.movies.domain.repository.Repositories
import com.evastos.movies.domain.repository.movie.overview.MovieOverviewRepository
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieOverviewRepository(
        movieOverviewRepository: MovieOverviewRepository
    ): Repositories.MovieOverviewRepository
}
