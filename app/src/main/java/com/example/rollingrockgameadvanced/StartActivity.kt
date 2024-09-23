package com.example.rollingrockgameadvanced

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch


private lateinit var start_BTN_fastmode : MaterialSwitch
private lateinit var main_BTN_start : MaterialButton
private lateinit var start_BTN_sensormode : MaterialSwitch
private lateinit var start_BTN_recordTable : MaterialButton



class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        askLocationPermissions(this)
        findViews()
        initViews()
        }

    private fun findViews() {
        start_BTN_fastmode = findViewById(R.id.start_BTN_fastmode)
        main_BTN_start = findViewById(R.id.main_BTN_start)
        start_BTN_sensormode = findViewById(R.id.start_BTN_sensormode)
        start_BTN_recordTable = findViewById(R.id.start_BTN_recordTable)

    }

    private fun initViews() {
        main_BTN_start.setOnClickListener { clickStartGame() }
        start_BTN_recordTable.setOnClickListener { moveToHighScoreTable() }
    }

    private fun clickStartGame() {
        var speedFlag = false
        var sensorFlag = false
        if (start_BTN_sensormode.isChecked()) sensorFlag = true
        if (start_BTN_fastmode.isChecked()) speedFlag = true
        changeScreenToMainActivity(sensorFlag, speedFlag)
    }

    private fun changeScreenToMainActivity(sensorFlag: Boolean, speedFlag: Boolean) {
        val intent = Intent(this, MainActivity::class.java);
        var bundle = Bundle()
        bundle.putBoolean("sensor", sensorFlag)
        bundle.putBoolean("speed", speedFlag)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun moveToHighScoreTable() {
        val intent = Intent(this, FinalActivity::class.java);
        startActivity(intent)
        finish()
    }

   private fun askLocationPermissions(activity: Activity?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    1
                )
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }
    }


}
