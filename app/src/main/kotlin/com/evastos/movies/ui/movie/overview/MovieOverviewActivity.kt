package com.evastos.movies.ui.movie.overview

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import com.evastos.movies.R
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.movie.overview.datasource.model.LoadingState
import com.evastos.movies.ui.movie.base.BaseActivity
import com.evastos.movies.ui.movie.overview.adapter.MoviesAdapter
import com.evastos.movies.ui.util.extensions.setGone
import com.evastos.movies.ui.util.extensions.setVisible
import kotlinx.android.synthetic.main.activity_movie_overview.loadingStateView
import kotlinx.android.synthetic.main.activity_movie_overview.movieOverviewRootView
import kotlinx.android.synthetic.main.activity_movie_overview.moviesRecyclerView

class MovieOverviewActivity : BaseActivity() {

    companion object {
        private const val COLUMNS_COUNT = 2
    }

    private lateinit var movieOverviewViewModel: MovieOverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_overview)
        movieOverviewViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieOverviewViewModel::class.java)
        moviesRecyclerView.layoutManager = GridLayoutManager(this, COLUMNS_COUNT)
        val adapter = MoviesAdapter(this)
        moviesRecyclerView.adapter = adapter
        movieOverviewViewModel.posts.observe(this, Observer<PagedList<Movie>> {
            adapter.submitList(it)
        })
        movieOverviewViewModel.networkState.observe(this, Observer {loadingState ->
            loadingState?.let {
                when (it) {
                    LoadingState.LOADED -> loadingStateView.setGone()
                    LoadingState.LOADING -> loadingStateView.setVisible()
                    LoadingState.ERROR -> {
                        loadingStateView.setGone()
                        showSnackbar(movieOverviewRootView, "Error", "Retry") {
                            movieOverviewViewModel.retry()
                        }
                    }
                }
            }
        })

        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_overview, menu)
        return true
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            // todo: use the query to search
        }
    }
}
