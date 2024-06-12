package com.movies.streamy.model.repository.abstraction

import com.movies.streamy.model.dataSource.local.table.UserEntity
import kotlinx.coroutines.flow.Flow

interface ISessionRepository {
    fun getCurrentUser(id: Int): Flow<UserEntity?>
}