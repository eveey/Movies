package com.evastos.movies.ui.util.region

import java.util.Locale

class RegionProvider {

    /**
     * Returns ISO 3166-1 2 uppercase letter region code if it is defined for the current system
     * Locale, null otherwise.
     */
    fun getSystemRegion(): String? {
        with(Locale.getDefault().country) {
            if (length == 2) {
                return toUpperCase()
            }
        }
        return null
    }
}
