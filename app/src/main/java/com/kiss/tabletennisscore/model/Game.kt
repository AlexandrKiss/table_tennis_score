package com.kiss.tabletennisscore.model

import android.os.Parcelable
import com.kiss.tabletennisscore.common.PlayerMarker
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game (
    val firstPlayer: Player,
    val secondPlayer: Player,
    var serving: PlayerMarker? = null,
    var winScore: Int = 0,
    var winner: PlayerMarker? = null
): Parcelable
