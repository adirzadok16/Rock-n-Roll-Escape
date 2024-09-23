package com.example.rollingrockgameadvanced.Utilities

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.rollingrockgameadvanced.Model.Score
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("SP_FILE", Context.MODE_PRIVATE)


    companion object {
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManager {
            return instance
                ?: throw IllegalStateException("SharedPreferencesManagerV3 must be initialized by calling init(context) before use.")
        }

    }

    fun putString(key: String, value: String) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }


    fun getPlayersFromMemory(): List<Score> {
        // bring the players from the memory
        val gson = Gson()
        val playerslistFromSPString = SharedPreferencesManager
            .getInstance()
            .getString("PLAYERS_DATA", "")
        Log.d("playlistFromSP", "playlistFromSP: " + playerslistFromSPString)
        if (!playerslistFromSPString.isNullOrEmpty()) {
            // Define the type for the list of Score
            val type = object : TypeToken<List<Score>>() {}.type
            // Deserialize the JSON string into a list of Score objects
            var scoreList: List<Score> = gson.fromJson(playerslistFromSPString, type)
            return scoreList
        }
        return mutableListOf()
    }
}