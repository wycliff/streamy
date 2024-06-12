package com.movies.streamy.model.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.streamy.model.dataSource.local.table.UnitEntity

@Dao
interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unitEntity: UnitEntity): Long

    @Query("SELECT * FROM unit WHERE building_id = :buildingId")
    fun getBuildingUnits(buildingId: String): List<UnitEntity>

    @Query("SELECT * FROM unit")
    fun getAllUnits(): List<UnitEntity>

    @Query("DELETE FROM unit")
    suspend fun deleteUnits()
}