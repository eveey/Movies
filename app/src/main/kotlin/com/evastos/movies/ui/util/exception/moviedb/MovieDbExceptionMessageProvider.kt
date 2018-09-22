package com.evastos.movies.ui.util.exception.moviedb

import android.content.Context
import com.evastos.movies.R
import com.evastos.movies.data.exception.moviedb.MovieDbException
import com.evastos.movies.inject.qualifier.AppContext
import com.evastos.movies.ui.util.exception.ExceptionMessageProviders
import javax.inject.Inject

class MovieDbExceptionMessageProvider
@Inject constructor(@AppContext private val context: Context) : ExceptionMessageProviders.MovieDb {

    override fun getMessage(exception: MovieDbException): String {
        var message = context.getString(R.string.api_error_general)
        if (exception is MovieDbException.ClientException) {
            message = exception.errorMessage ?: context.getString(R.string.api_error_client)
        }
        if (exception is MovieDbException.ServerException) {
            message = context.getString(R.string.api_error_server_unavailable)
        }
        if (exception is MovieDbException.NetworkException) {
            message = context.getString(R.string.api_error_network)
        }
        return message
    }

    override fun getMessage(throwable: Throwable): String {
        if (throwable is MovieDbException) {
            return getMessage(throwable)
        }
        return context.getString(R.string.api_error_general)
    }
}
