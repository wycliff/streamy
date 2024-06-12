package com.movies.streamy.di

import com.movies.streamy.model.repository.abstraction.IMoreRepository
import com.movies.streamy.model.repository.implementation.MoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class MoreBindingModule {
    @Binds
    abstract fun bindMoreImplModule(impl: MoreRepositoryImpl): IMoreRepository

}