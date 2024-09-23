package com.example.rollingrockgameadvanced.Model

data class Score(
    var name: String = "",
    var score: Int = 0,
    var lat: Double = 0.0,
    var lon: Double = 0.0

) : Comparable<Score> {

    override fun compareTo(other: Score): Int {
        return other.score.compareTo(this.score) // Sort from highest to lowest
    }
}
