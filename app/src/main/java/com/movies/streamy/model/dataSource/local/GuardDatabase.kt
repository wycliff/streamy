package com.movies.streamy.model.dataSource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.movies.streamy.model.dataSource.local.dao.BuildingDao
import com.movies.streamy.model.dataSource.local.dao.UnitDao
import com.movies.streamy.model.dataSource.local.dao.UserDao
import com.movies.streamy.model.dataSource.local.table.BuildingEntity
import com.movies.streamy.model.dataSource.local.table.UnitEntity
import com.movies.streamy.model.dataSource.local.table.UserEntity
import info.mqtt.android.service.room.Converters

@Database(
    entities = [
        UserEntity::class,
        UnitEntity::class,
        BuildingEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GuardDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun buildingDao():BuildingDao
    abstract fun unitDao(): UnitDao
}