package com.kiss.tabletennisscore.di

import com.kiss.tabletennisscore.data.repository.ResultRepository
import com.kiss.tabletennisscore.data.repository.impl.ResultRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesResultRepository(resultRepositoryImpl: ResultRepositoryImpl): ResultRepository =
        resultRepositoryImpl
}