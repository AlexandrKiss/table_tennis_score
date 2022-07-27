package com.kiss.tabletennisscore.di

import android.content.Context
import androidx.room.Room
import com.kiss.tabletennisscore.data.AppDatabase
import com.kiss.tabletennisscore.data.dao.ResultDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideResultDao(appDatabase: AppDatabase): ResultDao = appDatabase.getResultDao()

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Scoreboard"
        ).build()
    }
}