package com.movies.streamy.model.dataSource.local.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "local_id")
    val localId: Int = 0,
    @ColumnInfo(name ="phone")
    val phone: String?,
    @ColumnInfo(name = "token")
    val token: String?,
    @ColumnInfo(name = "createdAt")
    val createdAt: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name ="firstName")
    val firstName: String?,
    @ColumnInfo(name ="id_number")
    val id_number: String?,
    @ColumnInfo(name ="lastName")
    val lastName: String?,
    @ColumnInfo(name ="role")
    val role: String?,
    @ColumnInfo(name ="securityCompanyCode")
    val securityCompanyCode: String?,
    @ColumnInfo(name ="securityCompanyId")
    val securityCompanyId: String?,
    @ColumnInfo(name ="securityCompanyName")
    val securityCompanyName: String?,
    @ColumnInfo(name ="security_guard_id")
    val security_guard_id: String?,
    @ColumnInfo(name ="updatedAt")
    val updatedAt: String?,
    @ColumnInfo(name ="username")
    val username: String?
)