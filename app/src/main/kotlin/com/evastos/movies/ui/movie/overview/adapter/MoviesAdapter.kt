package com.evastos.movies.ui.movie.overview.adapter

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.evastos.movies.R
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.ui.util.extensions.inflate
import kotlinx.android.synthetic.main.recyclerview_item_movie.view.movieTitleTextView

class MoviesAdapter(private val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieItemViewHolder(parent.inflate(R.layout.recyclerview_item_movie))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieItemViewHolder) {
            holder.bind(getItem(position))
        } else {
            throw RuntimeException("Unknown view holder type")
        }
    }

    private inner class MovieItemViewHolder(movieItemView: View) :
        RecyclerView.ViewHolder(movieItemView) {

        fun bind(movie: Movie?) {
            movie?.let {
                with(itemView) {
                    movieTitleTextView.text = it.title
                }
            }
        }
    }
}