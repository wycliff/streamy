package com.movies.streamy.model.dataSource.abstraction

import com.haroldadmin.cnradapter.NetworkResponse
import com.movies.streamy.model.dataSource.network.data.request.LoginRequest
import com.movies.streamy.model.dataSource.network.data.response.ErrorResponse
import com.movies.streamy.model.dataSource.network.data.response.LoginResponse


interface IAuthDataSource {
    suspend fun login(reqBody: LoginRequest): NetworkResponse<LoginResponse, ErrorResponse>
}