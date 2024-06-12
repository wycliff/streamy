package com.movies.streamy.view.parking



sealed class ParkingViewState {

    /**
     * Network state loading.
     */
    object Loading : ParkingViewState()

    /**
     * Network state Success.
     */
    object Success : ParkingViewState()


    /**
     * Error occurred state.
     */
    data class Error(
        val errorMessage: String?,
        val stringResourceId: Int?,
        val errorCode: Int?
    ) : ParkingViewState()
}