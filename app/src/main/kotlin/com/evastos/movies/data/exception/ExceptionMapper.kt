package com.evastos.movies.data.exception

interface ExceptionMapper<out Exception : Throwable> {

    fun map(throwable: Throwable): Exception
}
