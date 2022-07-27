package com.kiss.tabletennisscore.data.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "results_board")
data class ResultEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val date: Date,
    val firstPlayerName: String,
    val secondPlayerName: String,
    val firstPlayerScore: Int,
    val secondPlayerScore: Int,
    val winner: String?
)
