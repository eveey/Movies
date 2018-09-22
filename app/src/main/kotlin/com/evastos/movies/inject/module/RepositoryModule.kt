package com.evastos.movies.inject.module

import com.evastos.movies.domain.Repositories
import com.evastos.movies.domain.movie.overview.MovieOverviewRepository
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductDetailsRepository(
        movieOverviewRepository: MovieOverviewRepository
    ): Repositories.MovieOverviewRepository
}
