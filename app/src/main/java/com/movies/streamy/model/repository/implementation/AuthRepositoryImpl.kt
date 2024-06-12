package com.movies.streamy.model.repository.implementation

import com.haroldadmin.cnradapter.NetworkResponse
import com.movies.streamy.model.dataSource.local.table.UserEntity
import com.movies.streamy.model.dataSource.abstraction.IUserCacheDataSource
import com.movies.streamy.model.dataSource.abstraction.IAuthDataSource
import com.movies.streamy.model.dataSource.network.data.request.LoginRequest
import com.movies.streamy.model.dataSource.network.data.response.ErrorResponse
import com.movies.streamy.model.dataSource.network.data.response.LoginResponse
import com.movies.streamy.model.repository.abstraction.IAuthRepository
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val userCacheDataSource: IUserCacheDataSource,
    private val authDataSource: IAuthDataSource,
) : IAuthRepository {
    override suspend fun login(reqBody: LoginRequest): NetworkResponse<LoginResponse, ErrorResponse> {
        return authDataSource.login(reqBody)
    }

    override suspend fun cacheUser(loginResponse: LoginResponse) {
        val userData = loginResponse.loginData

        val userEntity = UserEntity(
            phone = userData?.phone ,
            token = loginResponse.jsonWebToken,
            createdAt = userData?.createdAt,
            email = userData?.email,
            firstName =userData?.firstName ,
            id_number = userData?.id_number,
            lastName = userData?.lastName,
            role = userData?.role,
            securityCompanyCode = userData?.securityCompanyCode,
            securityCompanyId = userData?.securityCompanyId,
            securityCompanyName = userData?.securityCompanyName,
            security_guard_id = userData?.security_guard_id,
            updatedAt = userData?.updatedAt,
            username = userData?.username
        )

        userCacheDataSource.insertUser(userEntity)
    }
}