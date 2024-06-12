package com.movies.streamy.view

sealed class MainViewState {
    /**
     * Network state loading.
     */
    object Loading : MainViewState()

    /**
     * Network state Success.
     */
    object Success : MainViewState()


    /**
     * Error occurred state.
     */
    data class Error(
        val errorMessage: String?,
        val stringResourceId: Int?,
        val errorCode: Int?,
    ) : MainViewState()


    object someOtherOne : MainViewState()
}