package com.movies.streamy.model.dataSource.network.apiService

import com.haroldadmin.cnradapter.NetworkResponse
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
import retrofit2.http.Body
import retrofit2.http.POST

interface HomeApiInterface {
    @POST("onboarding/visit/get_visits")
    suspend fun getVisits(@Body jsonObject: CommonRequest): NetworkResponse<VisitsResponse, ErrorResponse>

    @POST("onboarding/ticket/get_tickets")
    suspend fun getTickets(@Body jsonObject: CommonRequest): NetworkResponse<TicketsResponse, ErrorResponse>

    @POST("onboarding/park/get_parking")
    suspend fun getParkingList(@Body jsonObject: CommonRequest): NetworkResponse<ParkingListResponse, ErrorResponse>

    @POST("onboarding/ticket/get_comments")
    suspend fun getComments(@Body jsonObject: CommentsRequest): NetworkResponse<CommentsResponse, ErrorResponse>

    @POST("onboarding/visit/sign_out")
    suspend fun signOutVisitor(@Body jsonObject: SignOutVisitorRequest): NetworkResponse<SignOutResponse, ErrorResponse>

    @POST("onboarding/visit/sign_in")
    suspend fun signInVisitor(@Body jsonObject: SignInRequest): NetworkResponse<SignOutResponse, ErrorResponse>

    @POST("onboarding/ticket/create")
    suspend fun addTicket(@Body ticketRequest: TicketRequest): NetworkResponse<CreateTicketResponse, ErrorResponse>

    @POST("onboarding/park/sign_in")
    suspend fun parkVehicle(@Body parkRequest: ParkRequest): NetworkResponse<ParkVehicleResponse, ErrorResponse>

    @POST("onboarding/ticket/comment")
    suspend fun addComment(@Body addCommentRequest: AddCommentRequest): NetworkResponse<AddCommentResponse, ErrorResponse>

    @POST("onboarding/visit/get_units")
    suspend fun fetchBuildings(@Body jsonObject: BuildingsRequest): NetworkResponse<BuildingsResponse, ErrorResponse>

    @POST("onboarding/park/check")
    suspend fun searchVehicle(@Body parkRequest: ParkCheckRequest): NetworkResponse<ParkingObject, ErrorResponse>
}