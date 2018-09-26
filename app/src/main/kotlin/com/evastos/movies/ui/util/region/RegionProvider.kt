package com.evastos.movies.ui.util.region

import java.util.Locale

open class RegionProvider {

    companion object {
        private const val REGION_CODE_VALID_LENGTH = 2
    }

    /**
     * Returns ISO 3166-1 2 uppercase letter region code if it is defined for the current system
     * Locale, null otherwise.
     */
    open fun getSystemRegion(): String? {
        with(Locale.getDefault().country) {
            if (length == REGION_CODE_VALID_LENGTH) {
                return toUpperCase()
            }
        }
        return null
    }
}
