package com.movies.streamy.model.repository.implementation


import com.haroldadmin.cnradapter.NetworkResponse
import com.movies.streamy.model.dataSource.abstraction.IHomeDataSource
import com.movies.streamy.model.dataSource.abstraction.IUserCacheDataSource
import com.movies.streamy.model.dataSource.local.table.BuildingEntity
import com.movies.streamy.model.dataSource.local.table.UnitEntity
import com.movies.streamy.model.dataSource.local.table.UserEntity
import com.movies.streamy.model.dataSource.network.data.request.AddCommentRequest
import com.movies.streamy.model.dataSource.network.data.request.ParkCheckRequest
import com.movies.streamy.model.dataSource.network.data.request.ParkRequest
import com.movies.streamy.model.dataSource.network.data.request.SignInRequest
import com.movies.streamy.model.dataSource.network.data.request.TicketRequest
import com.movies.streamy.model.dataSource.network.data.response.AddCommentResponse
import com.movies.streamy.model.dataSource.network.data.response.BuildingsResponse
import com.movies.streamy.model.dataSource.network.data.response.CommentsResponse
import com.movies.streamy.model.dataSource.network.data.response.CreateTicketResponse
import com.movies.streamy.model.dataSource.network.data.response.ErrorResponse
import com.movies.streamy.model.dataSource.network.data.response.ParkVehicleResponse
import com.movies.streamy.model.dataSource.network.data.response.ParkingListResponse
import com.movies.streamy.model.dataSource.network.data.response.ParkingObject
import com.movies.streamy.model.dataSource.network.data.response.SignOutResponse
import com.movies.streamy.model.dataSource.network.data.response.TicketsResponse
import com.movies.streamy.model.dataSource.network.data.response.VisitsResponse
import com.movies.streamy.model.repository.abstraction.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: IHomeDataSource,
    private val userCacheDataSource: IUserCacheDataSource,
) : IHomeRepository {

    override fun getCurrentUser(id: Int): Flow<UserEntity?> {
        return userCacheDataSource.getUserByLocalId(id)
    }

    override fun getBuildings(): List<BuildingEntity> {
        return homeDataSource.getBuildings()
    }

    override fun getUnits(buildingId: String): List<UnitEntity> {
        return homeDataSource.getUnits(buildingId)
    }

    override fun getAllUnits(): List<UnitEntity> {
        return homeDataSource.getAllUnits()
    }

    override suspend fun getVisits(securityGuardId: String?): NetworkResponse<VisitsResponse, ErrorResponse> {
        return homeDataSource.getVisits(securityGuardId)
    }

    override suspend fun getTickets(securityGuardId: String?): NetworkResponse<TicketsResponse, ErrorResponse> {
        return homeDataSource.getTickets(securityGuardId)
    }

    override suspend fun getParkingList(securityGuardId: String?): NetworkResponse<ParkingListResponse, ErrorResponse> {
        return homeDataSource.getParkingList(securityGuardId)
    }

    override suspend fun getComments(ticketsId: String?): NetworkResponse<CommentsResponse, ErrorResponse> {
        return homeDataSource.getComments(ticketsId)
    }

    override suspend fun signOutVisitor(
        securityGuardId: String?,
        visitId: String?,
        comment: String?
    ): NetworkResponse<SignOutResponse, ErrorResponse> {
        return homeDataSource.signOutVisitor(securityGuardId, visitId, comment)
    }

    override suspend fun signInVisitor(signInRequest: SignInRequest): NetworkResponse<SignOutResponse, ErrorResponse> {
        return homeDataSource.signInVisitor(signInRequest)
    }

    override suspend fun addTicket(ticketRequest: TicketRequest): NetworkResponse<CreateTicketResponse, ErrorResponse> {
        return homeDataSource.addTicket(ticketRequest)
    }

    override suspend fun parkVehicle(parkRequest: ParkRequest): NetworkResponse<ParkVehicleResponse, ErrorResponse> {
        return homeDataSource.parkVehicle(parkRequest)
    }

    override suspend fun searchVehicle(parkRequest: ParkCheckRequest): NetworkResponse<ParkingObject, ErrorResponse> {
        return homeDataSource.searchVehicle(parkRequest)
    }

    override suspend fun addComment(addCommentRequest: AddCommentRequest): NetworkResponse<AddCommentResponse, ErrorResponse> {
        return homeDataSource.addComment(addCommentRequest)
    }

    override suspend fun fetchBuildings(securityGuardId: String?): NetworkResponse<BuildingsResponse, ErrorResponse> {
        return homeDataSource.fetchBuildings(securityGuardId)
    }

    override suspend fun cacheBuildings(buildingsResponse: BuildingsResponse) {
        val buildings = buildingsResponse.buildings
        buildings?.let {
            for (building in buildings) {
                //save the buildings
                val buildingEntity = BuildingEntity(
                    buildingId = building?.buildingId.toString(),
                    buildingName = building?.buildingName,
                    noOfUnits = building?.noOfUnits
                )
                homeDataSource.insertBuilding(buildingEntity)

                //save the units
                val units = building?.units
                units?.let {
                    for (unit in units) {
                        unit?.let {
                            val unitEntity = UnitEntity(
                                unitId = it.unitId,
                                tenantId = it.tenantId,
                                tenantEmail = it.tenantEmail,
                                tenantName = it.tenantName,
                                tenantPhone = it.tenantPhone,
                                tenantUsername = it.tenantUsername,
                                unitName = it.unitName,
                                buildingId = building.buildingId,
                                ocupancyStatus = it.ocupancyStatus
                            )
                            insertUnit(unitEntity)
                        }
                    }
                }

            }
        }
    }

    override suspend fun insertUnit(unit: UnitEntity) {
        homeDataSource.insertUnit(unit)
    }
}