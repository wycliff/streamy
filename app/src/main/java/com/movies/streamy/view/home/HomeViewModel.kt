package com.movies.streamy.view.home

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
import com.movies.streamy.model.dataSource.network.data.request.AddCommentRequest
import com.movies.streamy.model.dataSource.network.data.request.ParkCheckRequest
import com.movies.streamy.model.dataSource.network.data.request.ParkRequest
import com.movies.streamy.model.dataSource.network.data.request.SignInRequest
import com.movies.streamy.model.dataSource.network.data.request.TicketRequest
import com.movies.streamy.model.dataSource.network.data.response.Comment
import com.movies.streamy.model.dataSource.network.data.response.ParkVehicleResponse
import com.movies.streamy.model.dataSource.network.data.response.ParkingObject
import com.movies.streamy.model.dataSource.network.data.response.SignOutResponse
import com.movies.streamy.model.dataSource.network.data.response.TicketsItem
import com.movies.streamy.model.dataSource.network.data.response.VisitsItem
import com.movies.streamy.model.repository.implementation.HomeRepositoryImpl
import com.movies.streamy.utils.AppUtil
import com.movies.streamy.view.parking.ParkingViewState
import com.movies.streamy.view.parking.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepositoryImpl,
    @IoDispatcher private val iODispatcher: CoroutineDispatcher,
) : ViewModel() {

    val currentUser: LiveData<UserEntity?>
        get() = homeRepository.getCurrentUser(0).asLiveData()

    private val _visits = MutableLiveData<List<VisitsItem>?>()
    val visits: LiveData<List<VisitsItem>?>
        get() = _visits

    private val _tickets = MutableLiveData<List<TicketsItem?>?>()
    val tickets: LiveData<List<TicketsItem?>?>
        get() = _tickets

    private val _signOut = MutableLiveData<SignOutResponse>()
    val signOut: LiveData<SignOutResponse>
        get() = _signOut

    private val _units = MutableLiveData<List<UnitEntity>>()
    val units: LiveData<List<UnitEntity>>
        get() = _units

    private val _buildings = MutableLiveData<List<BuildingEntity>>()
    val buildings: LiveData<List<BuildingEntity>>
        get() = _buildings


    private val _viewState = MutableLiveData<HomeViewState>()
    val viewState: LiveData<HomeViewState>
        get() = _viewState


    private val _parkingViewState = MutableLiveData<ParkingViewState>()
    val parkingViewState: LiveData<ParkingViewState>
        get() = _parkingViewState


    private val _parkedState = MutableLiveData<ParkVehicleResponse>()
    val parkedState: MutableLiveData<ParkVehicleResponse>
        get() = _parkedState

    private val _searchViewState = MutableLiveData<SearchViewState>()
    val searchViewState: LiveData<SearchViewState>
        get() = _searchViewState

    private val _vehicle = MutableLiveData<ParkingObject?>()
    val vehicle: LiveData<ParkingObject?>
        get() = _vehicle

    private val _comments = MutableLiveData<List<Comment?>?>()
    val comments: LiveData<List<Comment?>?>
        get() = _comments


    private val _parkingList = MutableLiveData<List<ParkingObject?>?>()
    val parkingList: LiveData<List<ParkingObject?>?>
        get() = _parkingList

    private val _isAdded = MutableLiveData<Boolean>()
    val isAdded: LiveData<Boolean>
        get() = _isAdded


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

    fun getAllBuildings() {
        viewModelScope.launch(iODispatcher) {
            val units = homeRepository.getBuildings()
            units.let {
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

    fun signOutVisitor(securityGuardId: String?, visitId: String?, comment: String?) {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.signOutVisitor(securityGuardId, visitId, comment)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    val data = result.body
                    _signOut.postValue(data)
                    getVisits(securityGuardId)
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

    fun signInVisitor(signInRequest: SignInRequest) {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.signInVisitor(signInRequest)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    val data = result.body
                    _signOut.postValue(data)
                    getVisits(signInRequest.security_guard?.security_guard_id.toString())
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

    fun fetchBuildings(securityGuardId: String?) {
        _viewState.postValue(HomeViewState.Loading)
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

    fun getTickets(
        securityGuardId: String?
    ) {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.getTickets(securityGuardId)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    _viewState.postValue(HomeViewState.Success)

                    val data = result.body
                    _tickets.postValue(data.data)
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

    fun addTicket(ticketRequest: TicketRequest) {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.addTicket(ticketRequest)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    getTickets(ticketRequest.securityGuardId.toString())
                    _viewState.postValue(HomeViewState.Success)
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

    fun getComments(
        ticketId: String?
    ) {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.getComments(ticketId)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    _viewState.postValue(HomeViewState.Success)

                    val data = result.body
                    _comments.postValue(data.comments)
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

    fun addComment(addCommentRequest: AddCommentRequest) {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.addComment(addCommentRequest)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    getComments(addCommentRequest.ticket_id)
                    _viewState.postValue(HomeViewState.Success)
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

    fun addVehicle(parkRequest: ParkRequest) {
        _parkingViewState.postValue(ParkingViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.parkVehicle(parkRequest)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    _parkingViewState.postValue(ParkingViewState.Success)
                    _parkedState.postValue(result.body)
                }

                is NetworkResponse.NetworkError -> {
                    _parkingViewState.postValue(
                        ParkingViewState.Error(
                            null,
                            R.string.network_error_msg,
                            null
                        )
                    )
                }

                is NetworkResponse.ServerError -> {
                    _parkingViewState.postValue(
                        ParkingViewState.Error(
                            AppUtil.getErrorResponse(result.body),
                            null,
                            null
                        )
                    )
                }

                is NetworkResponse.UnknownError -> {
                    _parkingViewState.postValue(
                        ParkingViewState.Error(
                            null,
                            R.string.unknown_error_msg,
                            null
                        )
                    )
                }

            }
        }
    }


    fun getParkingList(
        securityGuardId: String?
    ) {
        _viewState.postValue(HomeViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.getParkingList(securityGuardId)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    _viewState.postValue(HomeViewState.Success)

                    val data = result.body
                    _parkingList.postValue(data.parkingList)
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

    fun searchVehicle(parkCheckRequest: ParkCheckRequest) {
        _searchViewState.postValue(SearchViewState.Loading)
        viewModelScope.launch(iODispatcher) {
            val result = executeWithRetry(times = 3) {
                homeRepository.searchVehicle(parkCheckRequest)
            }

            when (result) {
                is NetworkResponse.Success -> {
                    _searchViewState.postValue(SearchViewState.Success)
                    _vehicle.postValue(result.body)
                }

                is NetworkResponse.NetworkError -> {
                    _searchViewState.postValue(
                        SearchViewState.Error(
                            null,
                            R.string.network_error_msg,
                            null
                        )
                    )
                }

                is NetworkResponse.ServerError -> {
                    _searchViewState.postValue(
                        SearchViewState.Error(
                            AppUtil.getErrorResponse(result.body),
                            null,
                            null
                        )
                    )
                }

                is NetworkResponse.UnknownError -> {
                    _searchViewState.postValue(
                        SearchViewState.Error(
                            null,
                            R.string.unknown_error_msg,
                            null
                        )
                    )
                }

            }
        }
    }

    fun vehicleAdded(securityGuardId: String?) {
        getParkingList(securityGuardId)
    }

}