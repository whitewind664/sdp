package com.github.gogetters.letsgo.activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.Player
import com.github.gogetters.letsgo.util.*
import java.util.*
import java.util.concurrent.CompletableFuture


class BluetoothActivity : AppCompatActivity() {
    private lateinit var listen: Button
    private lateinit var send: Button
    private lateinit var search: Button
    private lateinit var listView: ListView
    private lateinit var status: TextView
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var client: BluetoothClient
    private lateinit var server: BluetoothServer
    private lateinit var btProbe: BluetoothProbe

    private val foundDevices: MutableSet<BluetoothDevice> = mutableSetOf()
    private val deviceInfo: MutableMap<BluetoothDevice, String> = mutableMapOf()
    private var isServer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)


        listen = findViewById<View>(R.id.bluetooth_button_listen) as Button
        send = findViewById<View>(R.id.bluetooth_button_send) as Button
        listView = findViewById<View>(R.id.found_devices) as ListView
        status = findViewById<View>(R.id.status) as TextView
        search = findViewById<View>(R.id.bluetooth_button_listDevices) as Button

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        //TODO: catch case where device has no bt adapter

        if (!bluetoothAdapter.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH)
        }


        registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))

        implementListeners()
    }


    // Handles found bluetooth devices
    private val receiver = object : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context, intent: Intent) {

            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {

                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name

                    if (deviceName != null) {
                        CompletableFuture.supplyAsync {
                            Log.d("FUTURES FUTURES FUTURES", "first part starting: $deviceName")
                            btProbe.connect(device)
                        }.thenAcceptAsync {
                            Log.d("FUTURES FUTURES FUTURES", "second part starting: $deviceName")
                            deviceInfo[device] = it!!
                            foundDevices.add(device)
                            this@BluetoothActivity.runOnUiThread { listFound() }
                            Log.d("Futures", "done")
                        }
                    }

                }
            }
        }
    }

    //no idea how to add this via xml
    private fun implementListeners() {
        listView.onItemClickListener =
            OnItemClickListener { adapterView, _, i, _ ->
                client = BluetoothClient()
                val deviceName = adapterView.adapter.getItem(i) as String
                var serverDevice: BluetoothDevice? = null

                for (device in foundDevices) {
                    if (deviceInfo[device] == deviceName) {
                        serverDevice = device
                    }
                }
                if (serverDevice == null) {
                    throw Error(getString(R.string.connection_error))
                }


                Toast.makeText(
                    applicationContext,
                    getString(R.string.connection_start),
                    Toast.LENGTH_SHORT
                )
                    .show()
                client.connect(serverDevice, service)
                isServer = false
            }
        send.setOnClickListener { run { sendMessage() } }
        search.setOnClickListener { run { search() } }
        listen.setOnClickListener { run { launchServer() } }

    }


    /**
     *  Sends a message
     */

    private fun sendMessage() {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
            putExtra(GameActivity.EXTRA_KOMI, 5.5)
            val local = Player.PlayerTypes.BTLOCAL.name
            val remote = Player.PlayerTypes.BTREMOTE.name
            if (isServer) {
                putExtra(GameActivity.EXTRA_PLAYER_TYPES, arrayOf(local, remote))
            } else {
                putExtra(GameActivity.EXTRA_PLAYER_TYPES, arrayOf(remote, local))
            }
        }
        startActivity(intent)
    }


    /**
     * Launches a BT server
     */
    private fun launchServer() {

        //TODO: prevent double server launch
        val getVisible = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(getVisible, 0)

        server = BluetoothServer()
        Toast.makeText(this, getString(R.string.listen_bt), Toast.LENGTH_SHORT).show()
        server.connect(service)

        isServer = true
    }


    //TODO: refactor so that the whole list doesn't get refreshed each time
    /**
     * Lists found devices
     */
    fun listFound() {

        val list: ArrayList<String> = ArrayList()
        for (device in foundDevices) {
            val info = deviceInfo[device]
            if (info != null)
                list.add(info)
        }

        val adapter: ArrayAdapter<*> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }


    /**
     * search for BT devices
     */
    private fun search() {
        foundDevices.clear()
        listFound()
        showLocationPermission()
        btProbe = BluetoothProbe()
        Toast.makeText(applicationContext, getString(R.string.search_bt), Toast.LENGTH_SHORT).show()

        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.startDiscovery()
        } else {
            val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnOn, 0)
        }
    }


    /**
     * Used for requesting the location permission. Might as well be moved into utils
     */
    private fun showLocationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                getString(R.string.need_permission).explainBTPermission(
                    getString(R.string.bt_permission_explain),
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    REQUEST_PERMISSION_FINE_LOCATION
                )
            } else {
                PermissionUtils.requestPermission(
                    this,
                    REQUEST_PERMISSION_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    /**
     * More permission stuff, might be moved into utils
     */
    private fun String.explainBTPermission(
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@BluetoothActivity)
        builder.setTitle(this)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                PermissionUtils.requestPermission(
                    this@BluetoothActivity,
                    permissionRequestCode,
                    permission
                )
            }
        builder.create().show()
    }

    /**
     * More permission stuff, might be moved into utils
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_FINE_LOCATION -> if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, getString(R.string.permission_ok), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.permission_fail), Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
//        const val STATE_LISTENING = 1
//        const val STATE_CONNECTING = 2
//        const val STATE_CONNECTED = 3
//        const val STATE_CONNECTION_FAILED = 4
//        const val STATE_MESSAGE_RECEIVED = 5

        const val REQUEST_PERMISSION_FINE_LOCATION = 1
        const val REQUEST_ENABLE_BLUETOOTH = 1

        val service = BluetoothGTPService()


//        private val MY_UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")
//        private val APP_UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")
    }
}
