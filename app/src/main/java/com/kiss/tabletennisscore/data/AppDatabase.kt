package com.kiss.tabletennisscore.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kiss.tabletennisscore.data.converters.DateConverter
import com.kiss.tabletennisscore.data.dao.ResultDao
import com.kiss.tabletennisscore.data.entites.ResultEntity

@Database(entities = [ResultEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getResultDao(): ResultDao
}