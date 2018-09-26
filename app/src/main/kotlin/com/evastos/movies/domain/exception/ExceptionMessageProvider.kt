package com.evastos.movies.domain.exception

interface ExceptionMessageProvider<in Exception> {

    fun getMessage(exception: Exception): String

    fun getMessage(throwable: Throwable): String
}
