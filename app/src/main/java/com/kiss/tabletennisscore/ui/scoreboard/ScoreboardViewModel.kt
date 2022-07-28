package com.kiss.tabletennisscore.ui.scoreboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiss.tabletennisscore.common.GameStatus
import com.kiss.tabletennisscore.common.PlayerMarker
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
                PlayerMarker.FIRST -> firstPlayer.apply {
                        score = changeScore(event, score)
                    }
                PlayerMarker.SECOND -> secondPlayer.apply {
                        score = changeScore(event, score)
                    }
            }

            when(player.score) {
                in 0 until winScore -> {
                    if (isServing) {
                        serving = getServing(playerMarker)
                    }
                    _gameLiveData.postValue(GameStatus.Resume(this))
                }
                winScore -> {
                    winner = playerMarker
                    _gameLiveData.postValue(GameStatus.Finish(this))
                }
            }
        }
    }

    fun saveResult(game: Game) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addResult(game)
        }
    }

    private fun getServing(serving: PlayerMarker): PlayerMarker {
        val currentServing = game?.serving ?: serving
        return if (double == 1) {
            double = 0
            when(currentServing) {
                PlayerMarker.FIRST -> PlayerMarker.SECOND
                PlayerMarker.SECOND -> PlayerMarker.FIRST
            }
        } else {
            double++
            currentServing
        }
    }

    private fun changeScore(event: ScoreEvent, score: Int): Int =
        when (event) {
            ScoreEvent.UP -> score + 1
            ScoreEvent.DOWN -> score - 1
        }

    fun cancel() {
        _gameLiveData.postValue(GameStatus.Cancel)
    }
}