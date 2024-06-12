package com.movies.streamy.model.dataSource.network.apiService

import com.haroldadmin.cnradapter.NetworkResponse
import com.movies.streamy.model.dataSource.network.data.request.CommonRequest
import com.movies.streamy.model.dataSource.network.data.response.ErrorResponse
import com.movies.streamy.model.dataSource.network.data.response.MovieIdResponse
import com.movies.streamy.model.dataSource.network.data.response.TicketsResponse
import com.movies.streamy.model.dataSource.network.data.response.VisitsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeApiInterface {
    @POST("onboarding/visit/get_visits")
    suspend fun getVisits(@Body jsonObject: CommonRequest): NetworkResponse<VisitsResponse, ErrorResponse>

    @GET("3/movie/changes")
    suspend fun getMovieId(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String?
    ): NetworkResponse<MovieIdResponse, ErrorResponse>

}