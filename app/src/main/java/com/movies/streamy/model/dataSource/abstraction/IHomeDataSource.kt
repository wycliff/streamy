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
import com.movies.streamy.model.dataSource.network.data.response.MovieIdResponse
import com.movies.streamy.model.dataSource.network.data.response.ParkVehicleResponse
import com.movies.streamy.model.dataSource.network.data.response.ParkingListResponse
import com.movies.streamy.model.dataSource.network.data.response.ParkingObject
import com.movies.streamy.model.dataSource.network.data.response.SignOutResponse
import com.movies.streamy.model.dataSource.network.data.response.TicketsResponse
import com.movies.streamy.model.dataSource.network.data.response.VisitsResponse

interface IHomeDataSource {
    suspend fun getVisits(securityGuardId: String?): NetworkResponse<VisitsResponse, ErrorResponse>

    suspend fun getMovieIds(): NetworkResponse<MovieIdResponse, ErrorResponse>
}