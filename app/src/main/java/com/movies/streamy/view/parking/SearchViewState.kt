package com.movies.streamy.view.parking


sealed class SearchViewState {

    /**
     * Network state loading.
     */
    object Loading : SearchViewState()

    /**
     * Network state Success.
     */
    object Success : SearchViewState()


    /**
     * Error occurred state.
     */
    data class Error(
        val errorMessage: String?,
        val stringResourceId: Int?,
        val errorCode: Int?
    ) : SearchViewState()

}