package com.evastos.movies.ui.movie.overview

import android.annotation.SuppressLint
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import com.evastos.movies.R
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.model.LoadingState
import com.evastos.movies.inject.module.GlideApp
import com.evastos.movies.ui.base.network.connectivity.NetworkConnectivityObserver
import com.evastos.movies.ui.movie.base.BaseActivity
import com.evastos.movies.ui.movie.overview.adapter.MoviesAdapter
import com.evastos.movies.ui.util.extensions.debounceClicks
import com.evastos.movies.ui.util.extensions.setGone
import com.evastos.movies.ui.util.extensions.setVisible
import kotlinx.android.synthetic.main.activity_movie_overview.moviesRecyclerView
import kotlinx.android.synthetic.main.activity_movie_overview.networkConnectivityBanner
import kotlinx.android.synthetic.main.layout_item_loading_state.loadingStateErrorTextView
import kotlinx.android.synthetic.main.layout_item_loading_state.loadingStateErrorView
import kotlinx.android.synthetic.main.layout_item_loading_state.loadingStateLoadingView
import kotlinx.android.synthetic.main.layout_item_loading_state.loadingStateRetryButton

class MovieOverviewActivity : BaseActivity(), NetworkConnectivityObserver {

    companion object {
        private const val MOVIE_COLUMNS_NUM = 2
        private const val HIDE_LOADING_DELAY_MILLIS = 400L
    }

    private lateinit var viewModel: MovieOverviewViewModel

    private val handler = Handler()

    @SuppressLint("RxSubscribeOnError", "RxLeakedSubscription")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_overview)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieOverviewViewModel::class.java)

        moviesRecyclerView.layoutManager = GridLayoutManager(this, MOVIE_COLUMNS_NUM)
        val adapter = MoviesAdapter(GlideApp.with(this)) { movie: Movie? ->
            // open details screen
        }
        moviesRecyclerView.adapter = adapter

        viewModel.movies.observe(this, Observer<PagedList<Movie>> { moviesList ->
            adapter.submitList(moviesList)
        })

        viewModel.loadingState.observe(this, Observer { loadingState ->
            loadingState?.let { it ->
                when (it) {
                    is LoadingState.Loading -> {
                        loadingStateLoadingView.setVisible()
                        loadingStateErrorView.setGone()
                    }
                    is LoadingState.LoadingSuccess -> {
                        hideLoading {
                            loadingStateErrorView.setGone()
                        }
                    }
                    is LoadingState.LoadingError -> {
                        hideLoading {
                            loadingStateErrorView.setVisible()
                            loadingStateErrorTextView.text = it.errorMessage
                            loadingStateRetryButton.debounceClicks().subscribe {
                                viewModel.retry()
                            }
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

    override fun onNetworkConnectivityAcquired() {
        networkConnectivityBanner.setGone()
        viewModel.retry()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onNetworkConnectivityLost() {
        networkConnectivityBanner.setVisible()
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

    private fun hideLoading(afterAction: () -> Unit) {
        handler.postDelayed({
            loadingStateLoadingView.setGone()
            afterAction.invoke()
        }, HIDE_LOADING_DELAY_MILLIS)
    }
}
