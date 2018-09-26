package com.evastos.movies.data.encode

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EncoderTest {

    private lateinit var encoder: Encoder

    @Before
    fun setUp() {
        encoder = Encoder()
    }

    @Test
    fun encodeUrlQuery_withQuery_encodesUrlQuery() {
        val expectedQuery = "The+House+that+Jack+Built-that%27s%2Fit"

        val encodedQuery = encoder.encodeUrlQuery("The House that Jack Built-that's/it")

        assertEquals(expectedQuery, encodedQuery)
    }

    @Test
    fun encodeUrlQuery_withEmptyQuery_returnsSameQuery() {
        val expectedQuery = ""

        val encodedQuery = encoder.encodeUrlQuery("")

        assertEquals(expectedQuery, encodedQuery)
    }
}
