package com.evastos.movies.data.exception.moviedb

import com.evastos.movies.data.exception.ExceptionMappers
import com.evastos.movies.data.model.moviedb.error.MovieDbError
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MovieDbExceptionMapper
@Inject constructor(
    private val moshi: Moshi
) : ExceptionMappers.MovieDb {

    override fun map(throwable: Throwable): MovieDbException {
        var exception: MovieDbException = MovieDbException.UnknownException()
        if (throwable is SocketTimeoutException || throwable is UnknownHostException) {
            exception = MovieDbException.NetworkException()
        }
        if (throwable is ConnectException) {
            exception = MovieDbException.ServerException()
        }
        if (throwable is HttpException) {
            getExceptionFromResponse(throwable).let {
                exception = it
            }
        }
        return exception
    }

    private fun getExceptionFromResponse(httpException: HttpException): MovieDbException {
        val responseBody = httpException.response().errorBody()?.string()
        responseBody?.let { errorResponse ->
            moshi.adapter(MovieDbError::class.java).fromJson(errorResponse)?.let { error ->
                error.statusMessage?.let {
                    return MovieDbException.ClientException(it)
                }
            }
        }
        var exception: MovieDbException = MovieDbException.UnknownException()
        val statusCode = httpException.code()
        if (statusCode >= HttpURLConnection.HTTP_BAD_REQUEST
                && statusCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {
            exception = MovieDbException.ClientException()
        } else if (statusCode >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
            exception = MovieDbException.ServerException()
        }
        return exception
    }
}
