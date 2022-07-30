package com.kiss.tabletennisscore.ui.winner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kiss.tabletennisscore.R
import com.kiss.tabletennisscore.common.PlayerMarker
import com.kiss.tabletennisscore.databinding.DialogWinnerBinding
import com.kiss.tabletennisscore.ui.BaseDialogFragment
import com.kiss.tabletennisscore.ui.scoreboard.ScoreboardFragment.Companion.NEW_GAME
import com.kiss.tabletennisscore.ui.scoreboard.ScoreboardFragment.Companion.RESULT_BOARD

class WinnerDialogFragment: BaseDialogFragment() {
    private lateinit var binding: DialogWinnerBinding
    private val args by navArgs<WinnerDialogFragmentArgs>()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        binding = DialogWinnerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(args.game.winner) {
            setBackgroundColor(this)
            setWinnerName(this)
        }

        binding.newGame.setOnClickListener { newGameCallback() }
        binding.resultBoard.setOnClickListener { resultBoardCallback() }
        interceptionPressingBack { newGameCallback() }
    }

    private fun setBackgroundColor(winner: PlayerMarker?) {
        winner?.let {
            val backgroundColor = when(it) {
                PlayerMarker.FIRST -> R.color.electron_blue
                PlayerMarker.SECOND -> R.color.chi_gong
            }
            binding.root.setBackgroundColor(ResourcesCompat.getColor(resources, backgroundColor, null))
        }
    }

    private fun setWinnerName(winner: PlayerMarker?) {
        winner?.let {
            val name = when(it) {
                PlayerMarker.FIRST -> args.game.firstPlayer.name
                PlayerMarker.SECOND -> args.game.secondPlayer.name
            }
            binding.winName.text = "$name ${getString(R.string.scoreboard_win)}"
        }
    }

    private fun newGameCallback() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(NEW_GAME, true)
        dismiss()
    }

    private fun resultBoardCallback() {
        val action =
            WinnerDialogFragmentDirections.actionWinnerDialogFragmentToResultBoardFragment()
        with(findNavController()) {
            previousBackStackEntry?.savedStateHandle?.set(RESULT_BOARD, null)
            navigate(action)
        }
    }
}