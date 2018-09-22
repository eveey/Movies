package com.evastos.movies.inject.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.evastos.movies.inject.qualifier.ViewModelKey
import com.evastos.movies.inject.viewmodel.ViewModelFactory
import com.evastos.movies.ui.movie.details.MovieDetailsViewModel
import com.evastos.movies.ui.movie.overview.MovieOverviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieOverviewViewModel::class)
    abstract fun bindMovieOverviewViewModel(
        movieOverviewViewModel: MovieOverviewViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetailsViewModel(movieDetailsViewModel: MovieDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
