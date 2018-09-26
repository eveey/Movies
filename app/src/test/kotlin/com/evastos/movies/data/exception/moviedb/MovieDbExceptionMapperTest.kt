package com.evastos.movies.data.exception.moviedb

import com.evastos.movies.data.model.moviedb.error.MovieDbError
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MovieDbExceptionMapperTest {

    private lateinit var exceptionMapper: MovieDbExceptionMapper

    private val moshi = Moshi.Builder().build()

    @Before
    fun setUp() {
        exceptionMapper = MovieDbExceptionMapper(moshi)
    }

    @Test
    fun map_withSocketTimeoutException_returnsNetworkException() {
        val throwable = SocketTimeoutException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is MovieDbException.NetworkException)
    }

    @Test
    fun map_withUnknownHostException_returnsNetworkException() {
        val throwable = UnknownHostException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is MovieDbException.NetworkException)
    }

    @Test
    fun map_withConnectException_returnsServerException() {
        val throwable = ConnectException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is MovieDbException.ServerException)
    }

    @Test
    fun map_withMovieDbErrorBody_returnsServerException() {
        val errorMessage = "movieDbErrorMsg"
        val movieDbError = MovieDbError(errorMessage, 404)
        val response = getResponseErrorBody(movieDbError)

        val exception = exceptionMapper.map(HttpException(response))

        assertTrue(exception is MovieDbException.ClientException)
        assertEquals(errorMessage, (exception as MovieDbException.ClientException).errorMessage)
    }

    @Test
    fun map_withHttpException_with400StatusCode_returnsClientException() {
        val exception = exceptionMapper.map(getHttpException(HttpURLConnection.HTTP_BAD_REQUEST))

        assertTrue(exception is MovieDbException.ClientException)
    }

    @Test
    fun map_withHttpException_with415StatusCode_returnsClientException() {
        val exception =
                exceptionMapper.map(getHttpException(HttpURLConnection.HTTP_UNSUPPORTED_TYPE))

        assertTrue(exception is MovieDbException.ClientException)
    }

    @Test
    fun map_withHttpException_with500StatusCode_returnsServerException() {
        val exception = exceptionMapper.map(getHttpException(HttpURLConnection.HTTP_INTERNAL_ERROR))

        assertTrue(exception is MovieDbException.ServerException)
    }

    @Test
    fun map_withHttpException_with505StatusCode_returnsServerException() {
        val exception = exceptionMapper.map(getHttpException(HttpURLConnection.HTTP_VERSION))

        assertTrue(exception is MovieDbException.ServerException)
    }

    @Test
    fun map_withOtherException_returnsUnknownException() {
        val throwable = Throwable()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is MovieDbException.UnknownException)
    }

    private fun getHttpException(statusCode: Int): HttpException {
        val request = Request.Builder().url("http://192.168.1.7").build()
        return HttpException(
            Response.error<String>(
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    ""
                ),
                okhttp3.Response.Builder().code(statusCode)
                        .message("")
                        .request(request).protocol(Protocol.HTTP_1_1).build()
            )
        )
    }

    private fun getResponseErrorBody(movieDbError: MovieDbError): Response<String> {
        val request = Request.Builder().url("http://192.168.1.7").build()
        return Response.error(
            ResponseBody.create(
                MediaType.parse("txt/plain"),
                moshi.adapter(MovieDbError::class.java).toJson(movieDbError)
            ),
            okhttp3.Response.Builder().code(HttpURLConnection.HTTP_UNAVAILABLE)
                    .message("")
                    .request(request).protocol(Protocol.HTTP_1_1).build()
        )
    }
}
