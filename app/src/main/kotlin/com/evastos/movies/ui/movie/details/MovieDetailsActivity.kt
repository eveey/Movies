package com.evastos.movies.ui.movie.details

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.evastos.movies.R
import com.evastos.movies.ui.movie.base.BaseActivity

class MovieDetailsActivity : BaseActivity() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        movieDetailsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MovieDetailsViewModel::class.java)
    }
}
