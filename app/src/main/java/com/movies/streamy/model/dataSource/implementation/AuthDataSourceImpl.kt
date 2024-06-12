package com.movies.streamy.model.dataSource.implementation

import com.haroldadmin.cnradapter.NetworkResponse
import com.movies.streamy.model.dataSource.abstraction.IAuthDataSource
import com.movies.streamy.model.dataSource.network.apiService.AuthApiInterface
import com.movies.streamy.model.dataSource.network.data.request.LoginRequest
import com.movies.streamy.model.dataSource.network.data.response.ErrorResponse
import com.movies.streamy.model.dataSource.network.data.response.LoginResponse
import javax.inject.Inject

class AuthDataSourceImpl  @Inject constructor(
    private val authApiClient: AuthApiInterface,
) : IAuthDataSource {
    override suspend fun login(reqBody: LoginRequest): NetworkResponse<LoginResponse, ErrorResponse> {
        return authApiClient.login(reqBody)
    }
}