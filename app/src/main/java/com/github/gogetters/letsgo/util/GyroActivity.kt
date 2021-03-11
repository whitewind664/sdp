package com.github.gogetters.letsgo.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.gogetters.letsgo.R
import kotlin.math.abs

//TODO: allow sensorless devices
abstract class GyroActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private var mGyro: Sensor? = null
    private val gyroReading = FloatArray(3)
    private var gyroThreshold = 3 //TODO: replace with constant (to be defined elsewhere or here?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyro)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    override fun onResume() {
        super.onResume()
        mGyro?.also { gyro ->
            sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_GAME)
        }
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        print("accuracy changed")
    }

    fun setThreshold(threshold: Int){
        gyroThreshold = threshold
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if(event == null)
            return;

        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            // 3

            if(abs(event.values[1] - gyroReading[1]) > gyroThreshold){
                Log.d("GYRO", "FLIP DETECTED: difference: " + abs(event.values[1] - gyroReading[1]) + " rotation: " + gyroReading[1])
                rotationDetected()
            }

            System.arraycopy(event.values, 0, gyroReading, 0, gyroReading.size)
        }
    }

    //child class needs to decide what to do with the detected rotation
    abstract fun rotationDetected()
}