package com.kiss.tabletennisscore.ui.resultboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiss.tabletennisscore.common.gone
import com.kiss.tabletennisscore.databinding.ItemResultBinding
import com.kiss.tabletennisscore.model.Game
import java.text.SimpleDateFormat

class ResultBoardAdapter: RecyclerView.Adapter<ResultBoardAdapter.ResultHolder>() {
    private var gameList: List<Game>? = null

    inner class ResultHolder(binding: ItemResultBinding): RecyclerView.ViewHolder(binding.root) {
        val playerOneName = binding.playerOneName
        val playerTwoName = binding.playerTwoName
        val playerOneAvatar = binding.playerOneAvatar
        val playerTwoAvatar = binding.playerTwoAvatar
        val date = binding.date
        val score = binding.score
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResultBinding.inflate(inflater, parent, false)
        return ResultHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        gameList?.get(position)?.let { game ->
            holder.apply {
                playerOneName.text = game.firstPlayer.name
                playerTwoName.text = game.secondPlayer.name
                game.date?.let {
                    val dateFormat = SimpleDateFormat("dd MMMM, ''yy 'at' hh:mm a")
                    date.text = dateFormat.format(it)
                }
                score.text = "${game.firstPlayer.score} - ${game.secondPlayer.score}"
                playerOneAvatar.gone()
                playerTwoAvatar.gone()
            }
        }
    }

    override fun getItemCount(): Int = gameList?.size ?: 0

    fun setList(list: List<Game>) {
        gameList = list
        notifyDataSetChanged()
    }
}