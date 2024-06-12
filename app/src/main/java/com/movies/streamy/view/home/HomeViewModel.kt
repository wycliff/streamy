package com.movies.streamy.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.movies.streamy.R
import com.movies.streamy.di.IoDispatcher
import com.movies.streamy.model.dataSource.network.data.response.MovieId
import com.movies.streamy.model.dataSource.network.data.response.VisitsItem
import com.movies.streamy.model.repository.implementation.HomeRepositoryImpl
import com.movies.streamy.utils.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepositoryImpl,
    @IoDispatcher private val iODispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _visits = MutableLiveData<List<VisitsItem>?>()
    val visits: LiveData<List<VisitsItem>?>
        get() = _visits

    private val _movieIds = MutableLiveData<List<MovieId?>?>()
    val movieIds: LiveData<List<MovieId?>?>
        get() = _movieIds

    private val _viewState = MutableLiveData<HomeViewState>()
    val viewState: LiveData<HomeViewState>
        get() = _viewState


    fun getVisits(
        securityGuardId: String?
    ) {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.getVisits(securityGuardId)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    _viewState.postValue(HomeViewState.Success)

                    val data = result.body
                    _visits.postValue(data.visitData?.visit)
                }

                is NetworkResponse.NetworkError -> {
                    _viewState.postValue(
                        HomeViewState.Error(
                            null,
                            R.string.network_error_msg,
                            null
                        )
                    )
                }

                is NetworkResponse.ServerError -> {
                    _viewState.postValue(
                        HomeViewState.Error(
                            AppUtil.getErrorResponse(result.body),
                            null,
                            null
                        )
                    )
                }

                is NetworkResponse.UnknownError -> {
                    _viewState.postValue(
                        HomeViewState.Error(
                            null,
                            R.string.unknown_error_msg,
                            null
                        )
                    )
                }

            }
        }
    }


    fun getMovieIds() {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.getMovieIds()
            }

            when (result) {
                is NetworkResponse.Success -> {
                    _viewState.postValue(HomeViewState.Success)
                    val data = result.body

                    _movieIds.postValue(data.results)
                }

                is NetworkResponse.NetworkError -> {
                    _viewState.postValue(
                        HomeViewState.Error(
                            null,
                            R.string.network_error_msg,
                            null
                        )
                    )
                }

                is NetworkResponse.ServerError -> {
                    _viewState.postValue(
                        HomeViewState.Error(
                            AppUtil.getErrorResponse(result.body),
                            null,
                            null
                        )
                    )
                }

                is NetworkResponse.UnknownError -> {
                    _viewState.postValue(
                        HomeViewState.Error(
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