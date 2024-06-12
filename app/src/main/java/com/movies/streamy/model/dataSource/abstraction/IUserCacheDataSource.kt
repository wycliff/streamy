package com.movies.streamy.model.dataSource.abstraction

import com.movies.streamy.model.dataSource.local.table.UserEntity
import kotlinx.coroutines.flow.Flow

interface IUserCacheDataSource {
    suspend fun insertUser(userEntity: UserEntity): Long
    fun getUserByLocalId(localId: Int): Flow<UserEntity?>
    suspend fun deleteUser()
}