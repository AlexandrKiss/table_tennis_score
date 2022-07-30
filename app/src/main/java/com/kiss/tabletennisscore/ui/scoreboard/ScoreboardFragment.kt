package com.kiss.tabletennisscore.ui.scoreboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kiss.tabletennisscore.R
import com.kiss.tabletennisscore.common.*
import com.kiss.tabletennisscore.databinding.FragmentScoreboardBinding
import com.kiss.tabletennisscore.model.Player
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

@AndroidEntryPoint
class ScoreboardFragment: Fragment() {
    private lateinit var binding: FragmentScoreboardBinding
    private val viewModel: ScoreboardViewModel by viewModels()

    private val backDialog: AlertDialog by lazy { createAlertDialog() }

    private var firstName: String?
        get() = Preferences.load(PlayerMarker.FIRST.name, "").ifEmpty { null }
        set(value) = Preferences.save(PlayerMarker.FIRST.name, value ?: "")

    private var secondName: String?
        get() = Preferences.load(PlayerMarker.SECOND.name, "").ifEmpty { null }
        set(value) = Preferences.save(PlayerMarker.SECOND.name, value ?: "")

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        binding = FragmentScoreboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGame()
        initButtonListeners()
        initResetListener()
        initEditNameView()
        initOnBackPressCallback()
        initBackStackListener()
        view.hideKeyBoardListener()
    }

    private fun initGame() {
        binding.namePlayer1.setText(firstName)
        binding.namePlayer2.setText(secondName)
        initObservers()
        viewModel.initGame()
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
                    requireActivity().finish()
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

    private fun initResetListener() {
        binding.resetScore.setOnClickListener { reset() }
    }

    private fun initPlayersName() {
        val pNameOne = firstName ?: getString(R.string.player_one)
        val pNameTwo = secondName ?: getString(R.string.player_two)
        viewModel.setPlayersName(pNameOne, pNameTwo)
    }

    private fun initEditNameView() {
        val transparentColor = ColorStateList.valueOf(Color.TRANSPARENT)

        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
            if (!isOpen) {
                changeControlEnabled(true)
                with(binding.namePlayer1) {
                    backgroundTintList = transparentColor
                    clearFocus()
                }
                with(binding.namePlayer2) {
                    backgroundTintList = transparentColor
                    clearFocus()
                }
                initPlayersName()
            } else changeControlEnabled(false)
        }
        binding.namePlayer1.setCheckClearListener(Position.RIGHT) { name ->
            firstName = name
        }
        binding.namePlayer2.setCheckClearListener(Position.LEFT) { name ->
            secondName = name
        }
    }

    private fun EditText.setCheckClearListener(position: Position, callback: (String) -> Unit) {
        val removeIcon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_remove_text)

        setClearTextListener(position)
        addTextChangedListener {
            setRemoveIcon(removeIcon, position)
        }

        val transparentColor = ColorStateList.valueOf(Color.TRANSPARENT)
        val whiteColor = ColorStateList.valueOf(Color.WHITE)
        backgroundTintList = transparentColor
        setOnFocusChangeListener { _, isFocusable ->
            if (isFocusable) {
                backgroundTintList = whiteColor
                if ((text?.length ?: 0) > 0) setRemoveIcon(removeIcon, position)
            } else {
                backgroundTintList = transparentColor
                callback.invoke(text.toString())
                setRemoveIcon()
            }
        }
    }

    private fun EditText.setRemoveIcon(icon: Drawable? = null, position: Position? = null) {
        if (icon != null && position != null) {
            when(position) {
                Position.LEFT ->
                    setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
                Position.RIGHT ->
                    setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
            }
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.setClearTextListener(position: Position) {
        val clearTextListener = View.OnTouchListener { _, event ->
            if ((text?.length ?: 0) > 0) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val iconWidth =
                        compoundDrawables[0]?.bounds?.width() ?: compoundDrawables[2]?.bounds?.width() ?: 0
                    val iconOffset = when(position) {
                        Position.RIGHT -> right - iconWidth
                        Position.LEFT -> left + iconWidth * 5
                    }
                    val isIconClick = when(position) {
                        Position.RIGHT -> event.rawX >= iconOffset
                        Position.LEFT -> event.rawX <= iconOffset
                    }
                    if (isIconClick) {
                        text.clear()
                        setRemoveIcon()
                    }
                }
                return@OnTouchListener false
            } else {
                setRemoveIcon()
                return@OnTouchListener false
            }
        }
        setOnTouchListener(clearTextListener)
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

    private fun initBackStackListener() {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        val newGameLiveData = savedStateHandle?.getLiveData<Nothing>(NEW_GAME)
        val resultBoardLiveData = savedStateHandle?.getLiveData<Nothing>(RESULT_BOARD)

        newGameLiveData?.observe(viewLifecycleOwner) {
            with(binding) {
                namePlayer1.visible()
                namePlayer2.visible()
            }
            reset()
        }
        resultBoardLiveData?.observe(viewLifecycleOwner) {
            with(binding) {
                namePlayer1.invisible()
                namePlayer2.invisible()
            }
        }
    }

    private fun initOnBackPressCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backDialog.show()
        }
    }

    private fun createAlertDialog(): AlertDialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_message))
            .setPositiveButton(getString(R.string.alert_dialog_button_yes)) { _, _ ->
                viewModel.cancel()
            }.setNegativeButton(getString(R.string.alert_dialog_button_no)) { dialog, _ ->
                dialog.dismiss()
            }.create()

        dialog.setOnShowListener {
            val blue = ResourcesCompat.getColor(resources, R.color.electron_blue, null)
            val red = ResourcesCompat.getColor(resources, R.color.chi_gong, null)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(red)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(blue)
        }
        return dialog
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun View.hideKeyBoardListener() {
        setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                requireActivity().currentFocus?.let {
                    val imm = requireActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }

    companion object {
        const val NEW_GAME = "new_game"
        const val RESULT_BOARD = "result_board"
    }

    private enum class Position { LEFT, RIGHT }
}