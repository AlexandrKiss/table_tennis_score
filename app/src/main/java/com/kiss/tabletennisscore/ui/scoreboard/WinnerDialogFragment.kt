package com.kiss.tabletennisscore.ui.scoreboard

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kiss.tabletennisscore.R
import com.kiss.tabletennisscore.common.PlayerMarker
import com.kiss.tabletennisscore.databinding.DialogWinnerBinding
import com.kiss.tabletennisscore.ui.scoreboard.ScoreboardFragment.Companion.NEW_GAME
import com.kiss.tabletennisscore.ui.scoreboard.ScoreboardFragment.Companion.RESULT_BOARD

class WinnerDialogFragment: DialogFragment() {
    private lateinit var binding: DialogWinnerBinding
    private val args by navArgs<WinnerDialogFragmentArgs>()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        binding = DialogWinnerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backgroundColor = when(args.game.winner!!) {
                PlayerMarker.FIRST -> R.color.electron_blue
                PlayerMarker.SECOND -> R.color.chi_gong
            }
        binding.root.setBackgroundColor(ResourcesCompat.getColor(resources, backgroundColor, null))

        val name = when(args.game.winner!!) {
            PlayerMarker.FIRST -> args.game.firstPlayer.name
            PlayerMarker.SECOND -> args.game.secondPlayer.name
        }
        binding.winName.text = "$name ${getString(R.string.scoreboard_win)}"

        binding.newGame.setOnClickListener { newGameCallback() }
        binding.resultBoard.setOnClickListener { resultBoardCallback() }
    }

    override fun onResume() {
        removeDarkBackgroundAndSetNewSize()
        blockingBackgroundClickEvent()
        interceptionPressingBack()
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

    private fun interceptionPressingBack() {
        dialog?.setOnKeyListener { _, keyCode, _ ->
            return@setOnKeyListener if (keyCode == KeyEvent.KEYCODE_BACK) {
                newGameCallback()
                true
            } else false
        }
    }

    private fun newGameCallback() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(NEW_GAME, null)
        dismiss()
    }

    private fun resultBoardCallback() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(RESULT_BOARD, null)
        dismiss()
    }
}