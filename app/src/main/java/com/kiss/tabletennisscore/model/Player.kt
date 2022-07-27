package com.kiss.tabletennisscore.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class Player: Parcelable {
    abstract var name: String
    abstract var score: Int

    @Parcelize
    data class First (
        override var name: String = "",
        override var score: Int = 0
    ): Player()

    @Parcelize
    data class Second (
        override var name: String = "",
        override var score: Int = 0
    ): Player()
}