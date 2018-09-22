package com.evastos.movies.ui.util.exception

import com.evastos.movies.data.exception.moviedb.MovieDbException

interface ExceptionMessageProviders {

    interface MovieDb : ExceptionMessageProvider<MovieDbException>
}
