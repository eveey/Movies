package com.evastos.movies.inject.module

import android.app.Application
import android.content.Context
import com.evastos.movies.MoviesApp
import com.evastos.movies.inject.qualifier.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    @AppContext
    fun provideAppContext(): Context = MoviesApp.instance

    @Singleton
    @Provides
    fun provideApplication(): Application = MoviesApp.instance
}
