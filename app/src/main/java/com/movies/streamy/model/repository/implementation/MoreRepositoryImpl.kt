package com.movies.streamy.model.repository.implementation

import com.movies.streamy.model.dataSource.abstraction.IHomeDataSource
import com.movies.streamy.model.dataSource.abstraction.IUserCacheDataSource
import com.movies.streamy.model.repository.abstraction.IMoreRepository
import javax.inject.Inject

class MoreRepositoryImpl @Inject constructor(
    private val userCacheDataSource: IUserCacheDataSource,
    private val homeDataSource: IHomeDataSource,
) : IMoreRepository {
    override suspend fun logout() {
        userCacheDataSource.deleteUser()
        homeDataSource.deleteUnits()
        homeDataSource.deleteBuildings()
    }
}