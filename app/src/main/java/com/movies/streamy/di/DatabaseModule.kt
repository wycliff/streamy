package com.movies.streamy.di

import android.content.Context
import androidx.room.Room
import com.movies.streamy.model.dataSource.local.GuardDatabase
import com.movies.streamy.utils.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        GuardDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    fun provideUserDao(database: GuardDatabase) = database.userDao()

    @Provides
    fun provideUnitDao(database: GuardDatabase) = database.unitDao()

    @Provides
    fun provideBuildingDao(database: GuardDatabase) = database.buildingDao()

}