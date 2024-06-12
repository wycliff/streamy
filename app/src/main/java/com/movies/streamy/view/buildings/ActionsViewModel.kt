package com.movies.streamy.view.buildings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.movies.streamy.R
import com.movies.streamy.di.IoDispatcher
import com.movies.streamy.model.dataSource.local.table.BuildingEntity
import com.movies.streamy.model.dataSource.local.table.UnitEntity
import com.movies.streamy.model.dataSource.local.table.UserEntity
import com.movies.streamy.model.repository.implementation.HomeRepositoryImpl
import com.movies.streamy.utils.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActionsViewModel @Inject constructor(
    private val homeRepository: HomeRepositoryImpl,
    @IoDispatcher private val iODispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Orders"
    }
    val text: LiveData<String> = _text


    private val _viewState = MutableLiveData<BuildingsViewState>()
    val viewState: LiveData<BuildingsViewState>
        get() = _viewState


    private val _buildings = MutableLiveData<List<BuildingEntity>>()
    val buildings: LiveData<List<BuildingEntity>>
        get() = _buildings

    private val _units = MutableLiveData<List<UnitEntity>>()
    val units: LiveData<List<UnitEntity>>
        get() = _units

    val currentUser: LiveData<UserEntity?>
        get() = homeRepository.getCurrentUser(0).asLiveData()

    fun getBuildings(){
        viewModelScope.launch(iODispatcher) {
            val buildings = homeRepository.getBuildings()
            buildings?.let {
                _buildings.postValue(it)
            }
        }
    }


    fun getUnits(buildingId: String) {
        viewModelScope.launch(iODispatcher) {
            val units = homeRepository.getUnits(buildingId)
            units.let {
                _units.postValue(it)
            }
        }
    }

    fun fetchBuildingsFromApi(securityGuardId: String?){
        _viewState.postValue(BuildingsViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.fetchBuildings(securityGuardId)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    val data = result.body
                    homeRepository.cacheBuildings(data)
                }

                is NetworkResponse.NetworkError -> {
                    _viewState.postValue(
                        BuildingsViewState.Error(
                            null,
                            R.string.network_error_msg,
                            null
                        )
                    )
                }

                is NetworkResponse.ServerError -> {
                    _viewState.postValue(
                        BuildingsViewState.Error(
                            AppUtil.getErrorResponse(result.body),
                            null,
                            null
                        )
                    )
                }

                is NetworkResponse.UnknownError -> {
                    _viewState.postValue(
                        BuildingsViewState.Error(
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