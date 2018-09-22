package com.evastos.movies.data.exception

import com.evastos.movies.data.exception.moviedb.MovieDbException

interface ExceptionMappers {

    interface MovieDb : ExceptionMapper<MovieDbException>
}
