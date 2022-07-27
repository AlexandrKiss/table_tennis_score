package com.kiss.tabletennisscore.common

import android.view.View

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.changeVisibility(visible: Boolean?) {
    visibility = when(visible) {
        true -> View.VISIBLE
        false -> View.INVISIBLE
        null -> View.GONE
    }
}