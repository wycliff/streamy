package com.movies.streamy.model.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.streamy.model.dataSource.local.table.BuildingEntity


@Dao
interface BuildingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuilding(buildingEntity: BuildingEntity): Long

    @Query("SELECT * FROM building")
    fun getBuildings(): List<BuildingEntity>

    @Query("DELETE FROM building")
    suspend fun deleteBuildings()
}