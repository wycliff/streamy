package com.movies.streamy.view.buildings

sealed class BuildingsViewState {
    /**
     * Network state loading.
     */
    object Loading : BuildingsViewState()

    /**
     * Network state Success.
     */
    object Success : BuildingsViewState()

    /**
     * Error occurred state.
     */
    data class Error(
        val errorMessage: String?,
        val stringResourceId: Int?,
        val errorCode: Int?
    ) : BuildingsViewState()

}

