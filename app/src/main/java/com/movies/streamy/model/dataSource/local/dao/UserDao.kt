package com.movies.streamy.model.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.streamy.model.dataSource.local.table.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity): Long

    @Query("SELECT * FROM user WHERE local_Id = :localId")
    fun getCurrentUser(localId: Int): UserEntity?

    @Query("SELECT * FROM user WHERE local_Id = :localId")
    fun getUserByLocalId(localId: Int): Flow<UserEntity?>

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}