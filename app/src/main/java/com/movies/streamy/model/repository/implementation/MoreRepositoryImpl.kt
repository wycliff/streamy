package com.movies.streamy.model.repository.implementation

import com.movies.streamy.model.dataSource.abstraction.IHomeDataSource
import com.movies.streamy.model.dataSource.abstraction.IUserCacheDataSource
import com.movies.streamy.model.repository.abstraction.IMoreRepository
import javax.inject.Inject

class MoreRepositoryImpl @Inject constructor(

) : IMoreRepository {
    override suspend fun logout() {
    }
}