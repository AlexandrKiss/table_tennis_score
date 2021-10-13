package com.kiss.tabletennisscore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.kiss.tabletennisscore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val startText = "GO"
    private var double = 0
    private lateinit var playerOne: Player
    private lateinit var playerTwo: Player

    private var gameStatus = StatusGame.SERVING
    private var serving: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playerOne = Player(id = 0, scoreboard = binding.scorePlayer1, ball = binding.ballPlayer1)
        playerTwo = Player(id = 1, scoreboard = binding.scorePlayer2, ball = binding.ballPlayer2)

        resetScoreListener()
        binding.resetScore.setOnClickListener {
            resetScoreListener()
        }

        binding.upButtonPlayer1.setOnClickListener {
            when(gameStatus) {
                StatusGame.SERVING -> startGame(playerOne)
                StatusGame.GAME -> upPlayerScore(playerOne)
            }
        }
        binding.spareUpButtonPlayer1.setOnClickListener { spareUpPlayerScore(playerOne) }
        binding.downButtonPlayer1.setOnClickListener { downPlayerScore(playerOne) }

        binding.upButtonPlayer2.setOnClickListener {
            when(gameStatus) {
                StatusGame.SERVING -> startGame(playerTwo)
                StatusGame.GAME -> upPlayerScore(playerTwo)
            }
        }
        binding.spareUpButtonPlayer2.setOnClickListener { spareUpPlayerScore(playerTwo) }
        binding.downButtonPlayer2.setOnClickListener { downPlayerScore(playerTwo) }
    }

    private fun startGame(player: Player) {
        serving = player
        gameStatus = StatusGame.GAME
        player.setServing(true)
        playerOne.scoreboard.text = player.score.toString()
        playerTwo.scoreboard.text = player.score.toString()
        binding.resetScore.visibility = View.VISIBLE
        binding.spareUpButtonPlayer1.visibility = View.VISIBLE
        binding.spareUpButtonPlayer2.visibility = View.VISIBLE
        binding.downButtonPlayer1.visibility = View.VISIBLE
        binding.downButtonPlayer2.visibility = View.VISIBLE
    }

    private fun upPlayerScore(player: Player) {
        player.score++
        player.scoreboard.text = player.score.toString()
        if (double == 1) {
            serving!!.setServing(false)
            when(serving) {
                playerOne -> serving = playerTwo
                playerTwo -> serving = playerOne
            }
            serving!!.setServing(true)
            double = 0
        } else {
            double++
        }
    }

    private fun spareUpPlayerScore(player: Player) {
        if (gameStatus == StatusGame.GAME) {
            player.score++
            player.scoreboard.text = player.score.toString()
        }
    }

    private fun downPlayerScore(player: Player) {
        if (gameStatus == StatusGame.GAME && player.score > 0) {
            player.score--
            player.scoreboard.text = player.score.toString()
        }
    }

    private fun resetScoreListener() {
        binding.resetScore.visibility = View.INVISIBLE
        double = 0
        gameStatus = StatusGame.SERVING
        playerOne.score = 0
        playerOne.scoreboard.text = startText
        playerOne.setServing(false)
        binding.spareUpButtonPlayer1.visibility = View.INVISIBLE
        binding.downButtonPlayer1.visibility = View.INVISIBLE
        playerTwo.score = 0
        playerTwo.scoreboard.text = startText
        playerTwo.setServing(false)
        binding.spareUpButtonPlayer2.visibility = View.INVISIBLE
        binding.downButtonPlayer2.visibility = View.INVISIBLE
    }
}