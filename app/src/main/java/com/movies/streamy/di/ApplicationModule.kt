package com.movies.streamy.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class OnDataStoreRepositoryImplModule {
//    @Singleton
//    @Binds
//    abstract fun onDataStoreRepositoryImplModule(impl: DataStoreRepositoryImpl): IDataStoreRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideContext(application: Application): Context = application.applicationContext
//
//    @Provides
//    fun provideUploadApiInterface(
//        retrofit: Retrofit
//    ): UploadApiInterface = retrofit.create(UploadApiInterface::class.java)
//
//    @Provides
//    fun provideUploadDataSource(
//        service: UploadApiInterface
//    ): IUploadDataSource = UploadDataSourceImpl(service)

}

