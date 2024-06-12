package com.movies.streamy.model.dataSource.local.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unit")
data class UnitEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "unit_id")
    val unitId: String,
    @ColumnInfo(name = "tenant_id")
    val tenantId: String?,
    @ColumnInfo(name = "tenant_email")
    val tenantEmail: String?,
    @ColumnInfo(name = "tenant_name")
    val tenantName: String?,
    @ColumnInfo(name = "tenant_phone")
    val tenantPhone: String?,
    @ColumnInfo(name = "tenant_username")
    val tenantUsername: String?,
    @ColumnInfo(name = "unit_name")
    val unitName: String?,
    @ColumnInfo(name = "building_id")
    val buildingId: String?,
    @ColumnInfo(name = "ocupancy_status")
    val ocupancyStatus: String?
)

