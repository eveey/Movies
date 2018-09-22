package com.evastos.movies.inject.module

import com.evastos.movies.ui.util.region.RegionProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
class UtilModule {

    @Provides
    @Singleton
    fun provideRegionProvider(): RegionProvider {
        return RegionProvider()
    }
}
