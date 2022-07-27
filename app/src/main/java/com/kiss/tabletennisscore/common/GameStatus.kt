package com.kiss.tabletennisscore.common

import com.kiss.tabletennisscore.model.Game

sealed class GameStatus {
    class Init(val game: Game): GameStatus()
    class Resume(val game: Game): GameStatus()
    class Finish(val game: Game): GameStatus()
    object Cancel: GameStatus()
}