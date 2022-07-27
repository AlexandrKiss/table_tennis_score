package com.kiss.tabletennisscore.data.repository

import com.kiss.tabletennisscore.model.Game

interface ResultRepository {
    suspend fun addResult(game: Game)
    suspend fun getResultList(): List<Game>
}