package com.movies.streamy.view.home

sealed class HomeViewState {

    /**
     * Network state loading.
     */
    object Loading : HomeViewState()

    /**
     * Network state Success.
     */
    object Success : HomeViewState()


    /**
     * Error occurred state.
     */
    data class Error(
        val errorMessage: String?,
        val stringResourceId: Int?,
        val errorCode: Int?
    ) : HomeViewState()

}