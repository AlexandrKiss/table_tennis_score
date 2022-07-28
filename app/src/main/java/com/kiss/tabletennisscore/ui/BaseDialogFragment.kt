package com.kiss.tabletennisscore.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import com.kiss.tabletennisscore.R

open class BaseDialogFragment: DialogFragment() {

    override fun onResume() {
        removeDarkBackgroundAndSetNewSize()
        blockingBackgroundClickEvent()
        super.onResume()
    }

    private fun removeDarkBackgroundAndSetNewSize() {
        val window = dialog?.window
        val params = window?.attributes
        params?.let {
            it.width = resources.getDimensionPixelSize(R.dimen.button_size_500)
            it.height = resources.getDimensionPixelSize(R.dimen.button_size_250)
            it.dimAmount = 0.0f
        }
        window?.apply {
            attributes = params
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun blockingBackgroundClickEvent() {
        dialog?.apply {
            setCanceledOnTouchOutside(false)
        }
    }

    fun interceptionPressingBack(backCallback: () -> Unit) {
        dialog?.setOnKeyListener { _, keyCode, _ ->
            return@setOnKeyListener if (keyCode == KeyEvent.KEYCODE_BACK) {
                backCallback.invoke()
                true
            } else false
        }
    }
}