package com.movies.streamy.model.repository.implementation

import com.movies.streamy.model.dataSource.local.table.UserEntity
import com.movies.streamy.model.dataSource.abstraction.IUserCacheDataSource
import com.movies.streamy.model.repository.abstraction.ISessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val userCacheDataSource: IUserCacheDataSource,
) : ISessionRepository {
    override  fun getCurrentUser(id: Int): Flow<UserEntity?> {
        return userCacheDataSource.getUserByLocalId(id)
    }

}