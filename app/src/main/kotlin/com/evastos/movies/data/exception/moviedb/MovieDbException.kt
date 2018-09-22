package com.evastos.movies.data.exception.moviedb

sealed class MovieDbException : Throwable() {

    class ClientException(val errorMessage: String? = null) : MovieDbException()

    class ServerException : MovieDbException()

    class NetworkException : MovieDbException()

    class UnknownException : MovieDbException()
}
