package com.example.rollingrockgameadvanced.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rollingrockgameadvanced.Adapter.ScoreAdapter
import com.example.rollingrockgameadvanced.Model.Score
import com.example.rollingrockgameadvanced.R
import com.example.rollingrockgameadvanced.Utilities.SharedPreferencesManager
import com.example.rollingrockgameadvanced.databinding.FragmentHighScoreBinding
import com.example.rollingrockgameadvanced.interfaces.LoctionOfPlayerCallBack
import com.example.rollingrockgameadvanced.interfaces.ScoreClickedCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HighScoreFragment : Fragment() {

    var highScoreCallback: LoctionOfPlayerCallBack? = null
    private lateinit var binding : FragmentHighScoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentHighScoreBinding.inflate(inflater,container,false)
        val view=binding.root
        val scoreList=SharedPreferencesManager.getInstance().getPlayersFromMemory()

        val scoreAdapter=ScoreAdapter(scoreList)
        scoreAdapter.callback_ScoreCallback= object : ScoreClickedCallback {
            override fun scoreClicked(score: Score, position: Int) {
                highScoreCallback?.getLocation(score.lat,score.lon)
            }

        }
        binding.mainRCVScores.adapter=scoreAdapter

        val linearLayoutManager=LinearLayoutManager(this.context)
        linearLayoutManager.orientation= RecyclerView.VERTICAL
        binding.mainRCVScores.layoutManager=linearLayoutManager

        return view

    }
}


