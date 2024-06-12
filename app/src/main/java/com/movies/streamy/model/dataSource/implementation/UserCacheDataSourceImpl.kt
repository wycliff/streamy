package com.movies.streamy.model.dataSource.implementation

import com.movies.streamy.model.dataSource.local.table.UserEntity
import com.movies.streamy.model.dataSource.abstraction.IUserCacheDataSource
import com.movies.streamy.model.dataSource.local.dao.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserCacheDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : IUserCacheDataSource {
    override suspend fun insertUser(userEntity: UserEntity): Long {
        return userDao.insertUser(userEntity)
    }

    override fun getUserByLocalId(localId: Int): Flow<UserEntity?> {
        return userDao.getUserByLocalId(localId)
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }
}