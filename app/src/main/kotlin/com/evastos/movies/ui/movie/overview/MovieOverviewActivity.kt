package com.evastos.movies.ui.movie.overview

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import com.evastos.movies.R
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.domain.model.LoadingState
import com.evastos.movies.inject.module.GlideApp
import com.evastos.movies.ui.base.network.connectivity.NetworkConnectivityObserver
import com.evastos.movies.ui.movie.base.BaseActivity
import com.evastos.movies.ui.movie.details.MovieDetailsActivity
import com.evastos.movies.ui.movie.overview.adapter.MoviesAdapter
import com.evastos.movies.ui.util.extensions.debounceClicks
import com.evastos.movies.ui.util.extensions.setGone
import com.evastos.movies.ui.util.extensions.setVisible
import kotlinx.android.synthetic.main.activity_movie_overview.moviesRecyclerView
import kotlinx.android.synthetic.main.activity_movie_overview.networkConnectivityBanner
import kotlinx.android.synthetic.main.activity_movie_overview.swipeRefreshMovies
import kotlinx.android.synthetic.main.layout_item_loading_state.loadingStateErrorTextView
import kotlinx.android.synthetic.main.layout_item_loading_state.loadingStateErrorView
import kotlinx.android.synthetic.main.layout_item_loading_state.loadingStateLoadingView
import kotlinx.android.synthetic.main.layout_item_loading_state.loadingStateRetryButton

class MovieOverviewActivity : BaseActivity(), NetworkConnectivityObserver {

    companion object {
        private const val HIDE_LOADING_DELAY_MILLIS = 400L
        private const val MOVIE_COLUMNS_NUM = 2
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
            viewModel.onMovieClick(movie)
        }
        moviesRecyclerView.adapter = adapter

        swipeRefreshMovies.setOnRefreshListener {
            viewModel.onRefresh()
        }

        viewModel.moviesLiveData.observe(this, Observer<PagedList<Movie>> { moviesList ->
            adapter.submitList(moviesList)
        })

        viewModel.loadingStateLiveData.observe(this, Observer { loadingState ->
            loadingState?.let { it ->
                when (it) {
                    is LoadingState.Loading -> {
                        loadingStateLoadingView.setVisible()
                        loadingStateErrorView.setGone()
                    }
                    is LoadingState.LoadingSuccess -> {
                        hideLoading {
                            swipeRefreshMovies.isRefreshing = false
                            loadingStateErrorView.setGone()
                        }
                    }
                    is LoadingState.LoadingError -> {
                        hideLoading {
                            swipeRefreshMovies.isRefreshing = false
                            loadingStateErrorView.setVisible()
                            loadingStateErrorTextView.text = it.errorMessage
                            loadingStateRetryButton.debounceClicks().subscribe {
                                viewModel.onRetry()
                            }
                        }
                    }
                }
            }
        })

        viewModel.movieDetailsLiveData.observe(this, Observer { movie ->
            movie?.let {
                startActivity(MovieDetailsActivity.newIntent(this, it))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_overview, menu)

        val searchItem = menu.findItem(R.id.searchMoviesAction)
        val searchView = searchItem.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (!it.isEmpty()) {
                        viewModel.onSearchQuerySubmit(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    if (!it.isEmpty()) {
                        viewModel.onSearchQueryChange(it)
                    }
                }
                return true
            }
        })
        return true
    }

    override fun onNetworkConnectivityAcquired() {
        networkConnectivityBanner.setGone()
        viewModel.onRetry()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onNetworkConnectivityLost() {
        networkConnectivityBanner.setVisible()
    }

    private fun hideLoading(afterHide: () -> Unit) {
        handler.postDelayed({
            loadingStateLoadingView.setGone()
            afterHide.invoke()
        }, HIDE_LOADING_DELAY_MILLIS)
    }
}
