package com.github.gogetters.letsgo

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApi
import java.util.*


class BluetoothActivity : AppCompatActivity() {
    var b1: Button? = null
    var b2: Button? = null
    var b3: Button? = null
    var b4: Button? = null
    var b5: Button? = null

    private var BA: BluetoothAdapter? = null
    private var pairedDevices: Set<BluetoothDevice>? = null
    var lv: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        b1 = findViewById<View>(R.id.button) as Button
        b2 = findViewById<View>(R.id.button2) as Button
        b3 = findViewById<View>(R.id.button3) as Button
        b4 = findViewById<View>(R.id.button4) as Button
        b5 = findViewById<View>(R.id.button5) as Button
        BA = BluetoothAdapter.getDefaultAdapter()
        lv = findViewById<View>(R.id.listView) as ListView

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        BA!!.disable()
        BA!!.enable()
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action

            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address

                    Log.i("INFO", "name: $deviceName MAC: $deviceHardwareAddress")
                }
            }
        }
    }

    fun on(v: View?) {
        if (!BA!!.isEnabled) {
            val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnOn, 0)
            Toast.makeText(applicationContext, "Turned on", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "Already on", Toast.LENGTH_LONG).show()
        }
    }

    fun off(v: View?) {
        BA!!.disable()
        Toast.makeText(applicationContext, "Turned off", Toast.LENGTH_LONG).show()
    }

    fun visible(v: View?) {
        val getVisible = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(getVisible, 0)
    }

    fun search(v: View?){
        if(BA!!.isEnabled()){
            BA!!.startDiscovery()
            if(BA!!.isDiscovering())
                Toast.makeText(applicationContext, "Discovering devices", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "Bluetooth not enabled", Toast.LENGTH_LONG).show()

        }


    }

    fun listPaired(v: View?) {
        val pairedDevices: Set<BluetoothDevice> = BA!!.bondedDevices
        val list: ArrayList<String> = ArrayList<String>()
        for (bt in pairedDevices) list.add(bt.name)
        Toast.makeText(applicationContext, "Showing Paired Devices", Toast.LENGTH_SHORT).show()
        val adapter: ArrayAdapter<*> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        lv!!.adapter = adapter
    }
}