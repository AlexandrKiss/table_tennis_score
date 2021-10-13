package com.kiss.tabletennisscore

import android.view.View
import android.widget.ImageView
import android.widget.TextView

data class Player(
    private val id: Int,
    var score: Int = 0,
    private var serving: Boolean = false,
    val scoreboard: TextView,
    private val ball: ImageView
) {
    fun setServing(serving: Boolean) {
        this.serving = serving
        if (serving) {
            ball.visibility = View.VISIBLE
        } else {
            ball.visibility = View.INVISIBLE
        }
    }
}