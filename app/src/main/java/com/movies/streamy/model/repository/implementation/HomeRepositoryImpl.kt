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
import com.movies.streamy.model.dataSource.network.data.response.MovieIdResponse
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
) : IHomeRepository {

    override suspend fun getVisits(securityGuardId: String?): NetworkResponse<VisitsResponse, ErrorResponse> {
        return homeDataSource.getVisits(securityGuardId)
    }

    override suspend fun getMovieIds(): NetworkResponse<MovieIdResponse, ErrorResponse> {
        return homeDataSource.getMovieIds()
    }
}