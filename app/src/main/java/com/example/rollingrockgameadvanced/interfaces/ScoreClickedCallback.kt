package com.example.rollingrockgameadvanced.interfaces

import com.example.rollingrockgameadvanced.Model.Score

interface ScoreClickedCallback {
    fun scoreClicked(score: Score, position: Int)
}