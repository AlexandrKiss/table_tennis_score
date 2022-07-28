package com.kiss.tabletennisscore.ui.scoreboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kiss.tabletennisscore.R
import com.kiss.tabletennisscore.common.*
import com.kiss.tabletennisscore.databinding.FragmentScoreboardBinding
import com.kiss.tabletennisscore.model.Player
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreboardFragment: Fragment() {
    private lateinit var binding: FragmentScoreboardBinding
    private val viewModel: ScoreboardViewModel by viewModels()

    private val backDialog: AlertDialog by lazy { createAlertDialog() }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        binding = FragmentScoreboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGame()
        initButtonListeners()
        initResetListener()
        initOnBackPressCallback()
        initBackStackListener()
    }

    private fun initGame() {
        initObservers()
        viewModel.initGame()
    }

    private fun initPlayersName() {
        val pNameOne = getString(R.string.player_one)
        val pNameTwo = getString(R.string.player_two)
        viewModel.setPlayersName(pNameOne, pNameTwo)
    }

    private fun initObservers() {
        viewModel.gameLiveData.observe(viewLifecycleOwner) { status ->
            when(status) {
                is GameStatus.Init -> {
                    initPlayersName()
                    with(status.game) {
                        val goText = getString(R.string.scoreboard_go)
                        setScoreByPlayer(firstPlayer, goText)
                        setScoreByPlayer(secondPlayer, goText)
                    }
                    changeControlVisibility(false)
                    changeControlEnabled(true)
                }
                is GameStatus.Resume -> {
                    with(status.game) {
                        setScoreByPlayer(firstPlayer)
                        setScoreByPlayer(secondPlayer)
                        serving?.let { changeBallVisibility(it) }
                    }
                    if (!binding.controls.isVisible)
                        changeControlVisibility(true)
                }
                is GameStatus.Finish -> {
                    changeControlVisibility(false)
                    changeControlEnabled(false)
                    viewModel.saveResult(status.game)
                    val action = ScoreboardFragmentDirections
                        .actionScoreboardFragmentToWinnerDialogFragment(status.game)
                    findNavController().navigate(action)
                }
                is GameStatus.Cancel -> {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun initButtonListeners() {
        //Player 1
        binding.upButtonPlayer1.setOnClickListener {
            viewModel.changePlayerScore(PlayerMarker.FIRST, ScoreEvent.UP)
        }
        binding.spareUpButtonPlayer1.setOnClickListener {
            viewModel.changePlayerScore(PlayerMarker.FIRST, ScoreEvent.UP, false)
        }
        binding.downButtonPlayer1.setOnClickListener {
            viewModel.changePlayerScore(PlayerMarker.FIRST, ScoreEvent.DOWN, false)
        }

        //Player 2
        binding.upButtonPlayer2.setOnClickListener {
            viewModel.changePlayerScore(PlayerMarker.SECOND, ScoreEvent.UP)
        }
        binding.spareUpButtonPlayer2.setOnClickListener {
            viewModel.changePlayerScore(PlayerMarker.SECOND, ScoreEvent.UP, false)
        }
        binding.downButtonPlayer2.setOnClickListener {
            viewModel.changePlayerScore(PlayerMarker.SECOND, ScoreEvent.DOWN, false)
        }
    }

    private fun initBackStackListener() {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        val newGameLiveData = savedStateHandle?.getLiveData<Nothing>(NEW_GAME)
        val resultBoardLiveData = savedStateHandle?.getLiveData<Nothing>(RESULT_BOARD)

        newGameLiveData?.observe(viewLifecycleOwner) { reset() }
        resultBoardLiveData?.observe(viewLifecycleOwner) { reset() }
    }

    private fun initResetListener() {
        binding.resetScore.setOnClickListener { reset() }
    }

    private fun reset() {
        with(binding) {
            controls.invisible()
            ballPlayer1.invisible()
            ballPlayer2.invisible()
        }
        viewModel.initGame()
    }

    private fun setScoreByPlayer(players: Player, text: String? = null) {
        when (players) {
            is Player.First -> binding.scorePlayer1
            is Player.Second -> binding.scorePlayer2
        }.text = text ?: players.score.toString()
    }

    private fun changeBallVisibility(serving: PlayerMarker) {
        when (serving) {
            PlayerMarker.FIRST -> {
                binding.ballPlayer1.visible()
                binding.ballPlayer2.invisible()
            }
            PlayerMarker.SECOND -> {
                binding.ballPlayer1.invisible()
                binding.ballPlayer2.visible()
            }
        }
    }

    private fun changeControlVisibility(visible: Boolean?) {
        with(binding) {
            controls.changeVisibility(visible)
            resetScore.changeVisibility(visible)
        }
    }

    private fun changeControlEnabled(enabled: Boolean) {
        with(binding) {
            upButtonPlayer1.isEnabled = enabled
            upButtonPlayer2.isEnabled = enabled
        }
    }

    private fun createAlertDialog(): AlertDialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_message))
            .setPositiveButton(getString(R.string.alert_dialog_button_yes)) { _, _ ->
                viewModel.cancel()
            }.setNegativeButton(getString(R.string.alert_dialog_button_no)) { dialog, _ ->
                dialog.cancel()
            }.create()

    private fun initOnBackPressCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backDialog.show()
        }
    }

    companion object {
        const val NEW_GAME = "new_game"
        const val RESULT_BOARD = "result_board"
    }
}