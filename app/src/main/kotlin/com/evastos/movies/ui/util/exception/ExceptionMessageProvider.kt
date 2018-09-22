package com.evastos.movies.ui.util.exception

interface ExceptionMessageProvider<in Exception> {

    fun getMessage(exception: Exception): String

    fun getMessage(throwable: Throwable): String
}
