package com.kiss.tabletennisscore.model

import android.os.Parcelable
import com.kiss.tabletennisscore.common.GameTime
import com.kiss.tabletennisscore.common.PlayerMarker
import com.kiss.tabletennisscore.common.ScoreEvent
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Game (
    val firstPlayer: Player,
    val secondPlayer: Player,
    var serving: PlayerMarker? = null,
    var winScore: Int = 0,
    var winner: PlayerMarker? = null,
    var date: Date? = null,
    var gameTime: GameTime = GameTime.REGULAR_TIME
): Parcelable