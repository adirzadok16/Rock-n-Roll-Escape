package com.example.rollingrockgameadvanced

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.example.rollingrockgameadvanced.Fragments.HighScoreFragment
import com.example.rollingrockgameadvanced.Fragments.MapFragment
import com.example.rollingrockgameadvanced.Model.Score
import com.example.rollingrockgameadvanced.interfaces.LoctionOfPlayerCallBack
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FinalActivity : AppCompatActivity() {

    private lateinit var final_FRM_scorelist : FrameLayout
    private lateinit var final_FRM_map: FrameLayout
    private lateinit var  highScoreFragment: HighScoreFragment
    private lateinit var  mapFragment: MapFragment
    private lateinit var  final_BTN_return_button: AppCompatImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final) 
        // Make sure this layout contains the return_button
        findViews()
        setupFragments()
    }


    private fun findViews() {
        final_FRM_scorelist = findViewById(R.id.final_FRM_scorelist)
        final_FRM_map = findViewById(R.id.final_FRM_map)
        final_BTN_return_button = findViewById(R.id.final_BTN_return_button)

    }

    private fun setupFragments() {
        highScoreFragment = HighScoreFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.final_FRM_scorelist, highScoreFragment)
            .commit()

        highScoreFragment.highScoreCallback = object : LoctionOfPlayerCallBack{
            override fun getLocation(lat: Double, lon: Double) {
            mapFragment.changeLocation(lat,lon)
            }

        }

        mapFragment = MapFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.final_FRM_map, mapFragment)
            .commit()

        final_BTN_return_button.setOnClickListener { changeActivitytoMenu() }
    }

    private fun getPlayerListData(): List<Score> {
        val bundle: Bundle? = intent.extras
        val scoreListJson = bundle?.getString("scoreList")
        if (scoreListJson == null || scoreListJson.isEmpty()) {
            return mutableListOf()
        }
        return Gson().fromJson(
            scoreListJson,
            object : TypeToken<List<Score?>?>() {}.type
        )
    }

    private fun changeActivitytoMenu() {
        val intent = Intent(this, StartActivity::class.java)
        val b = Bundle()
        intent.putExtras(b)
        startActivity(intent)
        finish()
    }
}
