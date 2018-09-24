package com.evastos.movies.domain.model

/**
 * Indicates the loading state of the UI.
 */
sealed class LoadingState {
    class Loading : LoadingState()
    class LoadingSuccess : LoadingState()
    class LoadingError(val errorMessage: String) : LoadingState()
}
