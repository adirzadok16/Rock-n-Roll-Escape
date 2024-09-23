package com.example.rollingrockgameadvanced.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollingrockgameadvanced.Model.Score
import com.example.rollingrockgameadvanced.databinding.HorizontalHighscoreBinding
import com.example.rollingrockgameadvanced.interfaces.ScoreClickedCallback


class ScoreAdapter (private val scores: List<Score>): RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    var callback_ScoreCallback: ScoreClickedCallback? = null


    inner class ScoreViewHolder(val binding: HorizontalHighscoreBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.playerCARDData.setOnClickListener{
                if (callback_ScoreCallback != null)
                    callback_ScoreCallback!!.scoreClicked(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }
        }
    }
    fun getItem(position: Int) = scores[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = HorizontalHighscoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (scores.size > 10)
            return 10
        return scores.size
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        with(holder){
            with(scores[position]){
                binding.playerName.text = this.name
                binding.playerScore.text=this.score.toString()
            }

        }
    }
}