package com.movies.streamy.model.dataSource.local.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "building")
data class BuildingEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "building_id")
    val buildingId: String,
    @ColumnInfo(name = "building_name")
    val buildingName: String?,
    @ColumnInfo(name = "no_of_units")
    val noOfUnits: String?
)
