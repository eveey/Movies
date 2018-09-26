package com.evastos.movies.inject.module

import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.exception.moviedb.MovieDbExceptionMapper
import com.evastos.movies.domain.exception.ExceptionMessageProviders
import com.evastos.movies.domain.exception.moviedb.MovieDbExceptionMessageProvider
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ExceptionModule {

    @Binds
    abstract fun bindMovieDbExceptionMapper(
        movieDbExceptionMapper: MovieDbExceptionMapper
    ): ExceptionMappers.MovieDb

    @Binds
    abstract fun bindMovieDbExceptionMessageProvider(
        movieDbExceptionMessageProvider: MovieDbExceptionMessageProvider
    ): ExceptionMessageProviders.MovieDb
}
