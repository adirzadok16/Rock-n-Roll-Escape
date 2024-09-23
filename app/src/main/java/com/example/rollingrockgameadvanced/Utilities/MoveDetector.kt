package com.example.rollingrockgameadvanced.Utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.rollingrockgameadvanced.interfaces.Callback_moveCallback
import kotlin.math.abs

class MoveDetector(context: Context, private val moveCallback: Callback_moveCallback?) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private lateinit var sensorEventListener: SensorEventListener
    private var timestamp: Long = 0L

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                calculateMove(x,y)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //pass
            }
        }
    }


    private fun calculateMove(x: Float, y: Float) {
        if (System.currentTimeMillis() - timestamp >= 200) {
            timestamp = System.currentTimeMillis()
            if (x > 4.0) {
                moveCallback?.movingLeft()
            }
            if (x < -4.0) {
                moveCallback?.movingRight()
            }
        }
    }

    fun stop() {
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }

    fun start() {
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }
}



