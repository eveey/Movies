package com.evastos.movies

import com.evastos.movies.inject.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class MoviesApp : DaggerApplication() {

    companion object {
        lateinit var instance: MoviesApp
            private set
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.create()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initLogging()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
