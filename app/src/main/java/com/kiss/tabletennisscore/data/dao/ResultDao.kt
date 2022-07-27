package com.kiss.tabletennisscore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kiss.tabletennisscore.data.entites.ResultEntity

@Dao
interface ResultDao {

    @Insert
    fun insert(resultEntity: ResultEntity)

    @Query("SELECT * FROM results_board")
    fun getAllResults(): List<ResultEntity>
}