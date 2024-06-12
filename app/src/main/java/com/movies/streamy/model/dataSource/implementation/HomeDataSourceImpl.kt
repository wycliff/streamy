package com.movies.streamy.model.dataSource.implementation

import com.haroldadmin.cnradapter.NetworkResponse
import com.movies.streamy.model.dataSource.abstraction.IHomeDataSource
import com.movies.streamy.model.dataSource.local.dao.BuildingDao
import com.movies.streamy.model.dataSource.local.dao.UnitDao
import com.movies.streamy.model.dataSource.local.table.BuildingEntity
import com.movies.streamy.model.dataSource.local.table.UnitEntity
import com.movies.streamy.model.dataSource.network.apiService.HomeApiInterface
import com.movies.streamy.model.dataSource.network.data.request.AddCommentRequest
import com.movies.streamy.model.dataSource.network.data.request.BuildingsRequest
import com.movies.streamy.model.dataSource.network.data.request.CommentsRequest
import com.movies.streamy.model.dataSource.network.data.request.ParkRequest
import com.movies.streamy.model.dataSource.network.data.request.SignInRequest
import com.movies.streamy.model.dataSource.network.data.request.SignOutVisitorRequest
import com.movies.streamy.model.dataSource.network.data.request.TicketRequest
import com.movies.streamy.model.dataSource.network.data.request.CommonRequest
import com.movies.streamy.model.dataSource.network.data.request.ParkCheckRequest
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
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val homeApiInterface: HomeApiInterface,
    private val unitDao: UnitDao,
    private val buildingDao: BuildingDao
) : IHomeDataSource {
    override suspend fun getVisits(securityGuardId: String?): NetworkResponse<VisitsResponse, ErrorResponse> {
        val req = CommonRequest(securityGuardId)
        return homeApiInterface.getVisits(req)
    }

    override suspend fun getTickets(securityGuardId: String?): NetworkResponse<TicketsResponse, ErrorResponse> {
        val req = CommonRequest(securityGuardId)
        return homeApiInterface.getTickets(req)
    }

    override suspend fun getParkingList(securityGuardId: String?): NetworkResponse<ParkingListResponse, ErrorResponse> {
        val req = CommonRequest(securityGuardId)
        return homeApiInterface.getParkingList(req)
    }

    override suspend fun getComments(ticketsId: String?): NetworkResponse<CommentsResponse, ErrorResponse> {
        val req = CommentsRequest(ticketsId)
        return homeApiInterface.getComments(req)
    }

    override fun getBuildings(): List<BuildingEntity> {
        return buildingDao.getBuildings()
    }

    override fun getUnits(buildingId: String): List<UnitEntity> {
        return unitDao.getBuildingUnits(buildingId)
    }

    override fun getAllUnits(): List<UnitEntity> {
        return unitDao.getAllUnits()
    }

    override suspend fun deleteBuildings() {
        buildingDao.deleteBuildings()
    }

    override suspend fun deleteUnits() {
        unitDao.deleteUnits()
    }

    override suspend fun signOutVisitor(
        securityGuardId: String?,
        visitId: String?,
        comment: String?
    ): NetworkResponse<SignOutResponse, ErrorResponse> {
        val req = SignOutVisitorRequest(securityGuardId?.toInt(), visitId?.toInt(), comment)
        return homeApiInterface.signOutVisitor(req)
    }

    override suspend fun signInVisitor(signInRequest: SignInRequest): NetworkResponse<SignOutResponse, ErrorResponse> {
        return homeApiInterface.signInVisitor(signInRequest)
    }

    override suspend fun fetchBuildings(securityGuardId: String?): NetworkResponse<BuildingsResponse, ErrorResponse> {
        val req = BuildingsRequest(securityGuardId?.toInt())
        return homeApiInterface.fetchBuildings(req)
    }

    override suspend fun insertBuilding(buildingEntity: BuildingEntity): Long {
        return buildingDao.insertBuilding(buildingEntity)
    }

    override suspend fun insertUnit(unitEntity: UnitEntity): Long {
        return unitDao.insertUnit(unitEntity)
    }

    override suspend fun addTicket(ticketRequest: TicketRequest): NetworkResponse<CreateTicketResponse, ErrorResponse> {
        return homeApiInterface.addTicket(ticketRequest)
    }

    override suspend fun addComment(addCommentRequest: AddCommentRequest): NetworkResponse<AddCommentResponse, ErrorResponse> {
        return homeApiInterface.addComment(addCommentRequest)
    }

    override suspend fun parkVehicle(parkRequest: ParkRequest): NetworkResponse<ParkVehicleResponse, ErrorResponse> {
        return homeApiInterface.parkVehicle(parkRequest)
    }

    override suspend fun searchVehicle(parkRequest: ParkCheckRequest): NetworkResponse<ParkingObject, ErrorResponse> {
        return homeApiInterface.searchVehicle(parkRequest)
    }
}