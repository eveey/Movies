package com.evastos.movies.inject.module

import com.evastos.movies.BuildConfig
import com.evastos.movies.data.service.moviedb.MovieDbService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
class MovieDbModule {

    @Provides
    @Singleton
    fun provideRetrofit(okhttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_API_URL)
            client(okhttpClient)
            addConverterFactory(ScalarsConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(MoshiConverterFactory.create(moshi))
        }.build()
    }

    @Provides
    @Singleton
    fun provideMovieDbService(retrofit: Retrofit): MovieDbService {
        return retrofit.create(MovieDbService::class.java)
    }
}
