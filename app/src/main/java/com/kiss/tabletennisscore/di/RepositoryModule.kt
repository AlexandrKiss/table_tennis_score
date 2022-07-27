package com.kiss.tabletennisscore.di

import com.kiss.tabletennisscore.data.repository.ResultRepository
import com.kiss.tabletennisscore.data.repository.impl.ResultRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun providesResultRepository(impl: ResultRepositoryImpl): ResultRepository
}