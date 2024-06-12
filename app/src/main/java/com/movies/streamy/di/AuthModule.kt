package com.movies.streamy.di

import com.movies.streamy.model.dataSource.abstraction.IAuthDataSource
import com.movies.streamy.model.dataSource.network.apiService.AuthApiInterface
import com.movies.streamy.model.dataSource.implementation.AuthDataSourceImpl
import com.movies.streamy.model.repository.abstraction.IAuthRepository
import com.movies.streamy.model.repository.abstraction.ISessionRepository
import com.movies.streamy.model.repository.implementation.AuthRepositoryImpl
import com.movies.streamy.model.repository.implementation.SessionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object AuthModule {
    @Provides
    fun provideAuthApiService(
        retrofit: Retrofit
    ): AuthApiInterface = retrofit.create(AuthApiInterface::class.java)
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AuthBindingModule {

    @Binds
    abstract fun bindSessionRepositoryImpl(impl: SessionRepositoryImpl): ISessionRepository

    @Binds
    abstract fun bindAuthDataSourceImpl(impl: AuthDataSourceImpl): IAuthDataSource

    @Binds
    abstract fun bindAuthRepositoryImpl(impl: AuthRepositoryImpl): IAuthRepository
}