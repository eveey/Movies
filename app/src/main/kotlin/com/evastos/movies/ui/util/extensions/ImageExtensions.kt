package com.evastos.movies.ui.util.extensions

import android.net.Uri
import android.widget.ImageView
import com.evastos.movies.BuildConfig
import com.evastos.movies.R
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.inject.module.GlideRequests

fun GlideRequests.loadMoviePoster(movie: Movie?, imageView: ImageView) {
    if (movie?.posterPath != null) {
        load(Uri.parse("${BuildConfig.BASE_IMAGE_URL}${movie.posterPath}"))
                .fitCenter()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(imageView)
    } else {
        clear(imageView)
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView.setImageResource(R.drawable.ic_broken_image)
    }
}
