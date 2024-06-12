package com.movies.streamy.di

import com.movies.streamy.model.dataSource.abstraction.IUserCacheDataSource
import com.movies.streamy.model.dataSource.local.dao.UserDao
import com.movies.streamy.model.dataSource.implementation.UserCacheDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideUserCacheDataSource(
        userDao: UserDao,
    ): IUserCacheDataSource = UserCacheDataSourceImpl(userDao)


    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class MainBindingModule {

    //Bind singleton resources to a specific implementation
//    @Binds
//    abstract fun bindWeatherNetworkDataSourceImpl(impl: WeatherNetworkDataSourceImpl): IWeatherNetworkDataSource
}