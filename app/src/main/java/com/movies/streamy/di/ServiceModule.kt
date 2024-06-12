package com.movies.streamy.di

import com.movies.streamy.model.repository.abstraction.IServiceRepository
import com.movies.streamy.model.repository.implementation.ServiceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideServiceRepository(
//        broadcastNetworkDataSource: IBroadcastNetworkDataSource,
//        locationMetaDataCacheDataSource: ILocationMetaDataCacheDataSource,
//        closestCityCacheDataSource: IClosestCityCacheDataSource,
//        transporterCacheDataSource: ITransporterCacheDataSource,
//        dispatchOrderCacheDataSource: IDispatchOrderCacheDataSource,
//        driverCacheDataSource: IDriverCacheDataSource,
//        dispatchWaypointCacheDataSource: IDispatchWaypointCacheDataSource,
//        uploadDataSource: IUploadDataSource,
//        partnerCacheDataSource: IPartnerCacheDataSource,
//        instructionDispatchCacheDataSource: IInstructionDispatchCacheDataSource,
//        errandDispatchCacheDataSource: IErrandDispatchCacheDataSource,
//        broadcastCacheDataSource: IBroadCastCacheDataSource
    ): IServiceRepository = ServiceRepositoryImpl(
//        broadcastNetworkDataSource = broadcastNetworkDataSource,
//        locationMetaDataCacheDataSource = locationMetaDataCacheDataSource,
//        closestCityCacheDataSource = closestCityCacheDataSource,
//        cachedTransporterCacheDataSource = transporterCacheDataSource,
//        dispatchOrderCacheDataSource = dispatchOrderCacheDataSource,
//        driverCacheDataSource = driverCacheDataSource,
//        dispatchWaypointCacheDataSource = dispatchWaypointCacheDataSource,
//        uploadDataSource = uploadDataSource,
//        partnerCacheDataSource = partnerCacheDataSource,
//        instructionDispatchCacheDataSource = instructionDispatchCacheDataSource,
//        errandDispatchCacheDataSource = errandDispatchCacheDataSource,
//        broadcastCacheDataSource = broadcastCacheDataSource
    )
}