package com.evastos.movies.ui.movie.overview.adapter

import android.support.v7.util.DiffUtil
import com.evastos.movies.data.model.moviedb.Movie

object MovieDiffItemCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id === newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
