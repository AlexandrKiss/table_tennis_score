package com.kiss.tabletennisscore.ui.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiss.tabletennisscore.common.GameStatus
import com.kiss.tabletennisscore.common.GameTime
import com.kiss.tabletennisscore.common.PlayerMarker
import com.kiss.tabletennisscore.common.PlayerMarker.*
import com.kiss.tabletennisscore.common.ScoreEvent
import com.kiss.tabletennisscore.data.repository.ResultRepository
import com.kiss.tabletennisscore.model.Game
import com.kiss.tabletennisscore.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardViewModel @Inject constructor(private val repository: ResultRepository): ViewModel() {

    private var game: Game? = null
    private var double: Int = 0

    private val _gameLiveData = MutableLiveData<GameStatus>()
    val gameLiveData: LiveData<GameStatus>
        get() = _gameLiveData

    fun initGame() {
        game = Game(
            firstPlayer = Player.First(),
            secondPlayer = Player.Second(),
            winScore = 11
        )
        _gameLiveData.postValue(GameStatus.Init(game!!))
        double = 0
    }

    fun setPlayersName(fName: String? = null, sName: String? = null) {
        game?.let { game ->
            fName?.let { game.firstPlayer.name = it }
            sName?.let { game.secondPlayer.name = it }
        }
    }

    fun changePlayerScore(playerMarker: PlayerMarker, event: ScoreEvent, isServing: Boolean = true) {
        game?.apply {
            val player: Player = when(playerMarker) {
                FIRST -> firstPlayer.apply { score = changeScore(event, score) }
                SECOND -> secondPlayer.apply { score = changeScore(event, score) }
            }

            val anotherPlayerScore = when(player) {
                is Player.First -> secondPlayer.score
                is Player.Second -> firstPlayer.score
            }
            gameTime = getGameTime(player.score, anotherPlayerScore, winScore)

            if (isServing)
                serving = getServing(playerMarker, gameTime.serving)

            val isRegularTimeWinner =
                gameTime == GameTime.REGULAR_TIME && player.score == winScore
            val isOvertimeTimeWinner =
                gameTime == GameTime.OVERTIME && (player.score - anotherPlayerScore) == 2

            if (isRegularTimeWinner || isOvertimeTimeWinner) {
                winner = playerMarker
                _gameLiveData.postValue(GameStatus.Finish(this))
            } else
                _gameLiveData.postValue(GameStatus.Resume(this))
        }
    }

    fun saveResult(game: Game) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addResult(game)
        }
    }

    fun cancel() {
        _gameLiveData.postValue(GameStatus.Cancel)
    }

    private fun changeScore(event: ScoreEvent, score: Int): Int =
        when (event) {
            ScoreEvent.UP -> score + 1
            ScoreEvent.DOWN ->
                if (score != 0) score - 1
                else score
        }

    private fun getGameTime(firstScore: Int, secondScore: Int, winScore: Int): GameTime {
        return if (firstScore >= (winScore - 1) && secondScore >= (winScore - 1)) GameTime.OVERTIME
        else GameTime.REGULAR_TIME
    }

    private fun getServing(serving: PlayerMarker, ratio: Int): PlayerMarker {
        val currentServing = game?.serving ?: serving
        return if (double >= ratio) {
            double = 0
            when(currentServing) {
                FIRST -> SECOND
                SECOND -> FIRST
            }
        } else {
            double++
            currentServing
        }
    }
}