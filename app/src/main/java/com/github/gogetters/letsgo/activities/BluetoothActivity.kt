package com.github.gogetters.letsgo.activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.github.gogetters.letsgo.game.GTPCommand
import com.github.gogetters.letsgo.game.Player
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.util.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import kotlin.properties.Delegates


class BluetoothActivity: AppCompatActivity() {
    var listen: Button? = null
    var send: Button? = null
    var listDevices: Button? = null
    var listView: ListView? = null
    var msg_box: TextView? = null
    var status: TextView? = null
    var writeMsg: EditText? = null
    var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var client: BluetoothClient
    private lateinit var server: BluetoothServer
    private lateinit var btProbe: BluetoothProbe
    private var foundDevices: MutableSet<BluetoothDevice> = mutableSetOf()
    private var deviceInfo: MutableMap<BluetoothDevice, String> = mutableMapOf()
    private var isServer = false

    private val handler = Handler(Looper.getMainLooper()) {
        when (it.what) {
            0 -> {
                val bytes = it.obj as ByteArray
                val msg = bytes.decodeToString(0, bytes.indexOf(0))

                Log.d("BLUETOOTHTEST", "message received: $msg")
                msg_box!!.setText(msg)
                Toast.makeText(this, "$it.obj", Toast.LENGTH_LONG)
                true
            }
            else ->  {
                Log.d("BLUETOOTHTEST", "not working...")
                false
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)


        listen = findViewById<View>(R.id.bluetooth_button_listen) as Button
        send = findViewById<View>(R.id.bluetooth_button_send) as Button
        listView = findViewById<View>(R.id.listview) as ListView
        msg_box = findViewById<View>(R.id.msg) as TextView
        status = findViewById<View>(R.id.status) as TextView
        writeMsg = findViewById<View>(R.id.writemsg) as EditText
        listDevices = findViewById<View>(R.id.bluetooth_button_listDevices) as Button

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


        if (!bluetoothAdapter!!.isEnabled) {
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
            val action: String? = intent.action

            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name

                    if (deviceName != null) {
                            val futureInfo = CompletableFuture.supplyAsync {
                                Log.d("FUTURES FUTURES FUTURES", "first part starting: $deviceName")
                                btProbe.connect(device)
                            }.thenAcceptAsync {
                                Log.d("FUTURES FUTURES FUTURES", "second part starting: $deviceName")
                                deviceInfo[device] = it!!
                                foundDevices!!.add(device)
                                this@BluetoothActivity.runOnUiThread(Runnable {listFound()})
                                Log.d("FUTURESSSSSSSSSSSSSSS", "done")
                            }
                    }

                }
            }
        }
    }

    //no idea how to add this via xml
    private fun implementListeners() {
      listView!!.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                client = BluetoothClient()
                val deviceName = adapterView.adapter.getItem(i) as String
                var serverDevice: BluetoothDevice? = null

                for (device in foundDevices!!) {
                    if (deviceInfo[device] == deviceName) {
                        serverDevice = device
                    }
                }
                if (serverDevice == null) {
                    throw Error("DEVICE NOT FOUND")
                }
                Toast.makeText(applicationContext, "Establishing Connection", Toast.LENGTH_SHORT).show()
                client.connect(serverDevice, service)
                isServer = false
            }
    }


    /**
     *  Sends a message
     */

    fun sendMessage(v: View?) {
        val string = writeMsg!!.text.toString()
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
        Log.d("BLUETOOTH BLUETOOTH", "WE ARE SENDING A MOVE")
        service.sendCommand(GTPCommand.GENMOVE(Stone.WHITE))
        startActivity(intent)
    }


    /**
     * Launches a BT server
     */
    fun launchServer(v: View?){
        val getVisible = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(getVisible, 0)

        server = BluetoothServer(handler)
        Toast.makeText(this, "ESTABLISHING CONNECTION", Toast.LENGTH_SHORT)
        server.connect(service)
        isServer = true
    }

    /**
     * List devices user has paired with
     */
    fun listPaired() {
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter!!.bondedDevices
        val list: ArrayList<String> = ArrayList<String>()
        for (bt in pairedDevices) list.add(bt.name)
        Toast.makeText(applicationContext, "Showing Paired Devices", Toast.LENGTH_SHORT).show()
        val adapter: ArrayAdapter<*> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView!!.adapter = adapter
    }


    //TODO: refactor so that the whole list doesn't get refreshed each time
    /**
     * Lists found devices
     */
    fun listFound() {


        //Toast.makeText(applicationContext, "Showing Found Devices", Toast.LENGTH_SHORT).show()
        val list: ArrayList<String> = ArrayList<String>()
        for (device in foundDevices!!){
            val info = deviceInfo[device]
            if (info != null)
                list.add(info)
        }

        val adapter: ArrayAdapter<*> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView!!.adapter = adapter
    }


    /**
     * search for BT devices
     */
    fun search(v: View?){
        showLocationPermission()
        btProbe = BluetoothProbe()
        Toast.makeText(applicationContext, "Searching devices...", Toast.LENGTH_SHORT).show()

        if(bluetoothAdapter!!.isEnabled()){
            bluetoothAdapter!!.startDiscovery()
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
                this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Please allow accessing your location via the app settings in order to play via Bluetooth.", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_FINE_LOCATION)
            } else {
                PermissionUtils.requestPermission(this, REQUEST_PERMISSION_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    /**
     * More permission stuff, might be moved into utils
     */
    private fun showExplanation(title: String, message: String, permission: String, permissionRequestCode: Int) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, id -> PermissionUtils.requestPermission(this, permissionRequestCode, permission) })
        builder.create().show()
    }

    /**
     * More permission stuff, might be moved into utils
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_FINE_LOCATION -> if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        const val STATE_LISTENING = 1
        const val STATE_CONNECTING = 2
        const val STATE_CONNECTED = 3
        const val STATE_CONNECTION_FAILED = 4
        const val STATE_MESSAGE_RECEIVED = 5

        const val REQUEST_PERMISSION_FINE_LOCATION = 1
        const val REQUEST_ENABLE_BLUETOOTH = 1

        //TODO ??????????????????????????????????
        val service = BluetoothGTPService()


        //TODO: create own UUID
        private val MY_UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")
        private val APP_UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")
    }
}
