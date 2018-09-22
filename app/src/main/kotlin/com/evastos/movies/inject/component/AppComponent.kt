package com.evastos.movies.inject.component

import com.evastos.movies.inject.module.UtilModule
import com.evastos.movies.MoviesApp
import com.evastos.movies.inject.module.ActivityBuilder
import com.evastos.movies.inject.module.AppModule
import com.evastos.movies.inject.module.ExceptionModule
import com.evastos.movies.inject.module.MovieDbModule
import com.evastos.movies.inject.module.NetworkModule
import com.evastos.movies.inject.module.RepositoryModule
import com.evastos.movies.inject.module.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Suppress("unused")
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBuilder::class,
    AppModule::class,
    MovieDbModule::class,
    ViewModelModule::class,
    NetworkModule::class,
    ExceptionModule::class,
    UtilModule::class,
    RepositoryModule::class
])
interface AppComponent : AndroidInjector<MoviesApp>
