package com.evastos.movies.ui.movie.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.evastos.movies.BuildConfig
import com.evastos.movies.R
import com.evastos.movies.data.model.moviedb.Movie
import com.evastos.movies.inject.module.GlideApp
import com.evastos.movies.ui.base.network.connectivity.NetworkConnectivityObserver
import com.evastos.movies.ui.movie.base.BaseActivity
import com.evastos.movies.ui.util.extensions.loadImage
import com.evastos.movies.ui.util.extensions.setGone
import com.evastos.movies.ui.util.extensions.setVisible
import kotlinx.android.synthetic.main.activity_movie_details.movieBackdropImageView
import kotlinx.android.synthetic.main.activity_movie_details.movieOverviewTextView
import kotlinx.android.synthetic.main.activity_movie_details.movieTitleTextView
import kotlinx.android.synthetic.main.activity_movie_details.networkConnectivityBanner

class MovieDetailsActivity : BaseActivity(), NetworkConnectivityObserver {

    companion object {
        private const val EXTRA_MOVIE = "extraMovie"

        fun newIntent(context: Context, movie: Movie) =
                Intent(context, MovieDetailsActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MOVIE, movie)
                        }
    }

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        supportActionBar?.apply {
            title = getString(R.string.activity_title_movie_details)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieDetailsViewModel::class.java)
        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        viewModel.onCreate(movie)

        viewModel.backdropPhotoLiveData.observe(this, Observer { backdropPath ->
            val backdrop = if (backdropPath != null) {
                "${BuildConfig.BASE_IMAGE_URL}$backdropPath"
            } else {
                null
            }
            GlideApp.with(this).loadImage(backdrop, movieBackdropImageView)
        })
        viewModel.movieTitleLiveData.observe(this, Observer { movieTitle ->
            movieTitleTextView.text = movieTitle
        })
        viewModel.movieOverviewLiveData.observe(this, Observer { movieOverview ->
            movieOverviewTextView.text = movieOverview
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNetworkConnectivityAcquired() {
        networkConnectivityBanner.setGone()
    }

    override fun onNetworkConnectivityLost() {
        networkConnectivityBanner.setVisible()
    }
}
