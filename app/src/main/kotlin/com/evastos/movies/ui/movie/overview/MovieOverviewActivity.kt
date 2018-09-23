package com.evastos.movies.ui.movie.overview

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.evastos.movies.R
import com.evastos.movies.ui.movie.base.BaseActivity
import kotlinx.android.synthetic.main.activity_movie_overview.moviesRecyclerView

class MovieOverviewActivity : BaseActivity() {

    companion object {
        private const val COLUMNS_COUNT = 2
    }

    private lateinit var movieOverviewViewModel: MovieOverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_overview)
        moviesRecyclerView.layoutManager = GridLayoutManager(this, COLUMNS_COUNT)

        movieOverviewViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieOverviewViewModel::class.java)
    }
}
