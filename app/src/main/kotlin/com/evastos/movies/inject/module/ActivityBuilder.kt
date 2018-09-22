package com.evastos.movies.inject.module

import com.evastos.movies.ui.movie.details.MovieDetailsActivity
import com.evastos.movies.ui.movie.overview.MovieOverviewActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindMovieOverviewActivity(): MovieOverviewActivity

    @ContributesAndroidInjector
    internal abstract fun bindMovieDetailsActivity(): MovieDetailsActivity
}
