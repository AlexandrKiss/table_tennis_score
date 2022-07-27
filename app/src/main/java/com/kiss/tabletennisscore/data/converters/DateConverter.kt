package com.kiss.tabletennisscore.data.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun dateToLong(date: Date): Long = date.time


        @TypeConverter
        @JvmStatic
        fun longToDate(date: Long): Date = Date(date)
    }
}