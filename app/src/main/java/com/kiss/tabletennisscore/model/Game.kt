package com.kiss.tabletennisscore.model

import com.kiss.tabletennisscore.common.PlayerMarker

data class Game (
    val firstPlayer: Player,
    val secondPlayer: Player,
    var serving: PlayerMarker? = null,
    var winScore: Int = 0,
    var winner: PlayerMarker? = null
)
