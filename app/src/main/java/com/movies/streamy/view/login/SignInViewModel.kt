package com.movies.streamy.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.movies.streamy.R

import com.movies.streamy.di.IoDispatcher
import com.movies.streamy.model.dataSource.network.data.request.LoginRequest
import com.movies.streamy.model.repository.abstraction.IAuthRepository
import com.movies.streamy.utils.AppUtil.getErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    @IoDispatcher private val iODispatcher: CoroutineDispatcher,
    application: Application
) : AndroidViewModel(application) {

    private val _viewState = MutableLiveData<SignInViewState>()
    val viewState: LiveData<SignInViewState>
        get() = _viewState

    fun login(
       phone: String?,
       pin: String?
    ) {
        _viewState.postValue(SignInViewState.Loading)

        val loginRequest = LoginRequest(phone,pin,"security_guard")
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                authRepository.login(loginRequest)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    _viewState.postValue(SignInViewState.Success)

                    //save user data
                    val data = result.body
                    authRepository.cacheUser(data)
                }
                is NetworkResponse.NetworkError -> {
                    _viewState.postValue(
                        SignInViewState.LoginError(
                            null,
                            R.string.network_error_msg,
                            null
                        )
                    )
                }
                is NetworkResponse.ServerError -> {
                    _viewState.postValue(
                        SignInViewState.LoginError(
                            getErrorResponse(result.body),
                            null,
                            null
                        )
                    )
                }
                is NetworkResponse.UnknownError -> {
                    _viewState.postValue(
                        SignInViewState.LoginError(
                            null,
                            R.string.unknown_error_msg,
                            null
                        )
                    )
                }

            }
        }
    }

}