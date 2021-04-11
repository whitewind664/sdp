package com.github.gogetters.letsgo.activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.ArrayAdapter
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.util.PermissionUtils
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class BluetoothActivity: AppCompatActivity() {
    var listen: Button? = null
    var send: Button? = null
    var listDevices: Button? = null
    var listView: ListView? = null
    var msg_box: TextView? = null
    var status: TextView? = null
    var writeMsg: EditText? = null
    var bluetoothAdapter: BluetoothAdapter? = null
    lateinit var btArray: Array<BluetoothDevice?>
    private var sendReceive: SendReceive? = null
    private var foundDevices: MutableSet<BluetoothDevice>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        listen = findViewById<View>(R.id.listen) as Button
        send = findViewById<View>(R.id.send) as Button
        listView = findViewById<View>(R.id.listview) as ListView
        msg_box = findViewById<View>(R.id.msg) as TextView
        status = findViewById<View>(R.id.status) as TextView
        writeMsg = findViewById<View>(R.id.writemsg) as EditText
        listDevices = findViewById<View>(R.id.listDevices) as Button

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


        if (!bluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        foundDevices = mutableSetOf()
        registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))

        implementListeners()
    }


    // Handles found bluetooth devices
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

                    if (deviceName != null)
                        foundDevices!!.add(device)

                    listFound(null)
                }
            }
        }
    }

    //no idea how to add this via xml
    private fun implementListeners() {
      listView!!.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                val clientClass: ClientClass = ClientClass(btArray[i]!!)
                clientClass.start()
                status!!.text = "Connecting"
            }

    }

    // handles messages sent between client and server
    var handler = Handler { msg ->
        when (msg.what) {
            STATE_LISTENING -> status!!.text = "Listening"
            STATE_CONNECTING -> status!!.text = "Connecting"
            STATE_CONNECTED -> status!!.text = "Connected"
            STATE_CONNECTION_FAILED -> status!!.text = "Connection Failed"
            STATE_MESSAGE_RECEIVED -> {
                val readBuff = msg.obj as ByteArray
                val tempMsg = String(readBuff, 0, msg.arg1)
                msg_box!!.text = tempMsg
            }
        }
        true
    }


    /**
     *  Sends a message
     */

    fun sendMessage(v: View?){
        val string = writeMsg!!.text.toString()
        if(sendReceive != null)
            sendReceive!!.write(string.toByteArray())
        else
            Toast.makeText(applicationContext,
                    "Need to establish a connection first",
                    Toast.LENGTH_SHORT)
                    .show()
    }

    /**
     * Launches a BT server
     */
    fun launchServer(v: View?){
        val serverClass: ServerClass = ServerClass()
        serverClass.start()
    }

    /**
     * List devices user has paired with
     */
    fun listPaired(v: View?) {
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter!!.bondedDevices
        val list: ArrayList<String> = ArrayList<String>()
        for (bt in pairedDevices) list.add(bt.name)
        Toast.makeText(applicationContext, "Showing Paired Devices", Toast.LENGTH_SHORT).show()
        val adapter: ArrayAdapter<*> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView!!.adapter = adapter
    }


    /**
     * Lists found devices
     */
    fun listFound(v: View?) {
        val list: ArrayList<String> = ArrayList<String>()
        for (bt in foundDevices!!) list.add(bt.name)
        Toast.makeText(applicationContext, "Showing Found Devices", Toast.LENGTH_SHORT).show()
        val adapter: ArrayAdapter<*> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView!!.adapter = adapter
    }


    /**
     * search for BT devices
     */
    fun search(v: View?){
        showLocationPermission()

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


    /**
     * Would like to move this into utils as well, no idea how to handle the handler then
     */
    private inner class ServerClass : Thread() {
        private var serverSocket: BluetoothServerSocket? = null
        override fun run() {
            var socket: BluetoothSocket? = null
            while (socket == null) {
                try {
                    val message = Message.obtain()
                    message.what = Companion.STATE_CONNECTING
                    handler.sendMessage(message)
                    socket = serverSocket!!.accept()
                } catch (e: IOException) {
                    e.printStackTrace()
                    val message = Message.obtain()
                    message.what = Companion.STATE_CONNECTION_FAILED
                    handler.sendMessage(message)
                }
                if (socket != null) {
                    val message = Message.obtain()
                    message.what = Companion.STATE_CONNECTED
                    handler.sendMessage(message)
                    sendReceive = SendReceive(socket)
                    sendReceive!!.start()
                    break
                }
            }
        }

        init {
            try {
                serverSocket = bluetoothAdapter!!.listenUsingRfcommWithServiceRecord(
                    Companion.APP_NAME,
                    Companion.MY_UUID
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * Same here
     */
    private inner class ClientClass(private val device: BluetoothDevice) : Thread() {
        private var socket: BluetoothSocket? = null
        override fun run() {
            try {
                socket!!.connect()
                val message = Message.obtain()
                message.what = Companion.STATE_CONNECTED
                handler.sendMessage(message)
                sendReceive = SendReceive(socket!!)
                sendReceive!!.start()
            } catch (e: IOException) {
                e.printStackTrace()
                val message = Message.obtain()
                message.what = Companion.STATE_CONNECTION_FAILED
                handler.sendMessage(message)
            }
        }

        init {
            try {
                socket = device.createRfcommSocketToServiceRecord(Companion.MY_UUID)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    /**
     *
     */
    private inner class SendReceive(private val bluetoothSocket: BluetoothSocket) : Thread() {
        private val inputStream: InputStream?
        private val outputStream: OutputStream?
        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int
            while (true) {
                try {
                    bytes = inputStream!!.read(buffer)
                    handler.obtainMessage(
                        Companion.STATE_MESSAGE_RECEIVED,
                        bytes,
                        -1,
                        buffer
                    ).sendToTarget()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun write(bytes: ByteArray?) {
            try {
                outputStream!!.write(bytes)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        init {
            var tempIn: InputStream? = null
            var tempOut: OutputStream? = null
            try {
                tempIn = bluetoothSocket.inputStream
                tempOut = bluetoothSocket.outputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }
            inputStream = tempIn
            outputStream = tempOut
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

        //TODO: remove this
        private const val APP_NAME = "BTChat"
        //TODO: create own UUID
        private val MY_UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")
    }
}
