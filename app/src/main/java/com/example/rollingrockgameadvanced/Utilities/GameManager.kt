package com.example.rollingrockgameadvanced.Utilities

import android.util.Log
import com.example.rollingrockgameadvanced.Model.Score
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GameManager(private val lifeCount: Int = 3) {

    private var playerList: ArrayList<Score> = ArrayList<Score>()
        private set

    var score: Int = 0
        private set


    var wrongAnswers: Int = 0
        private set

    val isGameLost: Boolean
        get() = wrongAnswers == lifeCount

    fun onCollision() {
        wrongAnswers++
    }

    fun AddToScore(num: Int): Int {
        score += num
        return score
    }


    fun addPlayer(score: Score) {
        // add score to the list
        playerList.add(score)
        // sort the list
        playerList.sort()
        if (playerList.size > 10) { // if list is to long take the first 10 top scores
            this.playerList.take(10)
        }
        //save to memory
        val gson = Gson()
        val playlistAsJson = gson.toJson(this.playerList)
        SharedPreferencesManager.getInstance().putString("PLAYERS_DATA", playlistAsJson)
    }

    fun getPlayersFromMemory() {
        // bring the players from the memory
        val gson = Gson()
        val playerslistFromSPString = SharedPreferencesManager
            .getInstance()
            .getString("PLAYERS_DATA", "")
        Log.d("playlistFromSP", "playlistFromSP: " + playerslistFromSPString)

        val type = object : TypeToken<ArrayList<Score>>() {}.type
        val playerslistFromSP: ArrayList<Score>? = gson.fromJson(playerslistFromSPString, type)
        Log.d("playlistObjFromSP", "playlistObjFromSP: " + playerslistFromSP)
        if (playerslistFromSP != null) {
            playerList = playerslistFromSP
        }
    }
}




