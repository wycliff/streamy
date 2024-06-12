package com.movies.streamy.model.dataSource.abstraction

import com.haroldadmin.cnradapter.NetworkResponse
import com.movies.streamy.model.dataSource.local.table.BuildingEntity
import com.movies.streamy.model.dataSource.local.table.UnitEntity
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

interface IHomeDataSource {
    suspend fun getVisits(securityGuardId: String?): NetworkResponse<VisitsResponse, ErrorResponse>

    suspend fun getTickets(securityGuardId: String?): NetworkResponse<TicketsResponse, ErrorResponse>

    suspend fun getParkingList(securityGuardId: String?): NetworkResponse<ParkingListResponse, ErrorResponse>

    suspend fun getComments(ticketsId: String?): NetworkResponse<CommentsResponse, ErrorResponse>

    fun getBuildings(): List<BuildingEntity>

    fun getUnits(buildingId: String): List<UnitEntity>

    fun getAllUnits(): List<UnitEntity>

    suspend fun deleteBuildings()

    suspend fun deleteUnits()

    suspend fun signOutVisitor(securityGuardId: String?, visitId: String?, comment: String?): NetworkResponse<SignOutResponse, ErrorResponse>

    suspend fun signInVisitor(signInRequest: SignInRequest): NetworkResponse<SignOutResponse, ErrorResponse>

    suspend fun fetchBuildings(securityGuardId: String?): NetworkResponse<BuildingsResponse, ErrorResponse>

    suspend fun insertBuilding(buildingEntity: BuildingEntity): Long

    suspend fun insertUnit(unitEntity: UnitEntity): Long

    suspend fun addTicket(ticketRequest: TicketRequest): NetworkResponse<CreateTicketResponse, ErrorResponse>

    suspend fun addComment(addCommentRequest: AddCommentRequest): NetworkResponse<AddCommentResponse, ErrorResponse>

    suspend fun parkVehicle(parkRequest: ParkRequest): NetworkResponse<ParkVehicleResponse, ErrorResponse>

    suspend fun searchVehicle(parkRequest: ParkCheckRequest): NetworkResponse<ParkingObject, ErrorResponse>

}