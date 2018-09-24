package com.evastos.movies.ui.movie.overview.adapter

import android.annotation.SuppressLint
import android.arch.paging.PagedListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.evastos.movies.R
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.inject.module.GlideRequests
import com.evastos.movies.ui.util.extensions.debounceClicks
import com.evastos.movies.ui.util.extensions.inflate
import com.evastos.movies.ui.util.extensions.loadMoviePoster
import kotlinx.android.synthetic.main.layout_item_movie.view.moviePosterImageButton

class MoviesAdapter(
    private val glideRequests: GlideRequests,
    private val movieClickAction: (Movie?) -> Unit
) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieItemViewHolder(parent.inflate(R.layout.layout_item_movie))
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

        @SuppressLint("RxLeakedSubscription", "RxSubscribeOnError")
        fun bind(movie: Movie?) {
            with(itemView) {
                glideRequests.loadMoviePoster(movie, moviePosterImageButton)
                moviePosterImageButton.debounceClicks().subscribe { _ ->
                    movie?.let {
                        movieClickAction.invoke(it)
                    }
                }
            }
        }
    }
}
