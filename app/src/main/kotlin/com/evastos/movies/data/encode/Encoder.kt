package com.evastos.movies.data.encode

import java.net.URLEncoder

class Encoder {

    companion object {
        private const val UTF_8_DATA_FORMAT = "UTF-8"
    }

    fun encodeUrlQuery(query: String): String = URLEncoder.encode(query, UTF_8_DATA_FORMAT)
}
