package com.movies.streamy.model.dataSource.network.apiService

import com.haroldadmin.cnradapter.NetworkResponse
import com.movies.streamy.model.dataSource.network.data.request.LoginRequest
import com.movies.streamy.model.dataSource.network.data.response.ErrorResponse
import com.movies.streamy.model.dataSource.network.data.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiInterface {
    @POST("onboarding/user/login")
    suspend fun login(@Body jsonObject: LoginRequest): NetworkResponse<LoginResponse, ErrorResponse>
}