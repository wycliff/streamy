package com.movies.streamy.view.login

sealed class SignInViewState {

    /**
     * Network state loading.
     */
    object Loading : SignInViewState()

    /**
     * Network state Success.
     */
    object Success : SignInViewState()


    /**
     * Error occurred state.
     */
    data class LoginError(
        val errorMessage: String?,
        val stringResourceId: Int?,
        val errorCode: Int?
    ) : SignInViewState()

}