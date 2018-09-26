package com.evastos.movies.domain.exception

import com.evastos.movies.data.exception.moviedb.MovieDbException

interface ExceptionMessageProviders {

    interface MovieDb : ExceptionMessageProvider<MovieDbException>
}
