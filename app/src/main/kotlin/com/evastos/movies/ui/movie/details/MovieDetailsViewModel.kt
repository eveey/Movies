package com.evastos.movies.ui.movie.details

import android.arch.lifecycle.MutableLiveData
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.ui.movie.base.BaseViewModel
import javax.inject.Inject

class MovieDetailsViewModel
@Inject constructor(
) : BaseViewModel() {

    val backdropPhotoLiveData = MutableLiveData<String>()
    val movieTitleLiveData = MutableLiveData<String>()
    val movieOverviewLiveData = MutableLiveData<String>()

    fun onCreate(movie: Movie) {
        backdropPhotoLiveData.value = movie.backdropPath
        movieTitleLiveData.value = movie.title
        movieOverviewLiveData.value = movie.overview
    }
}
