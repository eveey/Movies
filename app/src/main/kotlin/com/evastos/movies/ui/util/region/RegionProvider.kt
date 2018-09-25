package com.evastos.movies.ui.util.region

import java.util.Locale

class RegionProvider {

    companion object {
        private const val VALID_REGION_CODE_LENGTH = 2
    }

    /**
     * Returns ISO 3166-1 2 uppercase letter region code if it is defined for the current system
     * Locale, null otherwise.
     */
    fun getSystemRegion(): String? {
        with(Locale.getDefault().country) {
            if (length == VALID_REGION_CODE_LENGTH) {
                return toUpperCase()
            }
        }
        return null
    }
}
