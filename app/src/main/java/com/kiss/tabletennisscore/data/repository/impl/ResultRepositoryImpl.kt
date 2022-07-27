package com.kiss.tabletennisscore.data.repository.impl

import com.kiss.tabletennisscore.common.PlayerMarker
import com.kiss.tabletennisscore.data.dao.ResultDao
import com.kiss.tabletennisscore.data.entites.ResultEntity
import com.kiss.tabletennisscore.data.repository.ResultRepository
import com.kiss.tabletennisscore.model.Game
import com.kiss.tabletennisscore.model.Player
import java.util.*
import javax.inject.Inject

class ResultRepositoryImpl @Inject constructor(private val dataBase: ResultDao): ResultRepository {
    override suspend fun addResult(game: Game) {
        with(game) {
            val resultEntity = ResultEntity(
                date = Calendar.getInstance().time,
                firstPlayerName = firstPlayer.name,
                secondPlayerName = secondPlayer.name,
                firstPlayerScore = firstPlayer.score,
                secondPlayerScore = secondPlayer.score,
                winner = when(winner) {
                    PlayerMarker.FIRST -> firstPlayer.name
                    PlayerMarker.SECOND -> secondPlayer.name
                    else -> null
                }
            )
            dataBase.insert(resultEntity)
        }
    }

    override suspend fun getResultList(): List<Game> {
        val resultList = dataBase.getAllResults()
        return resultList.map {
            val winner =
                if (it.firstPlayerScore > it.secondPlayerScore) PlayerMarker.SECOND
                else PlayerMarker.SECOND
            Game(
                firstPlayer = Player.First(it.firstPlayerName, it.firstPlayerScore),
                secondPlayer = Player.Second(it.secondPlayerName, it.secondPlayerScore),
                winner = winner
            )
        }
    }
}