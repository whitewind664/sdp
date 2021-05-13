package com.github.gogetters.letsgo.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothClient {

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var connectThread: ConnectThread
    private lateinit var service: BluetoothService


    fun connect(device: BluetoothDevice, service: BluetoothService) {
        this.service = service
        connectThread = ConnectThread(device)
        connectThread.start()
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        private val uuid: UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createInsecureRfcommSocketToServiceRecord(uuid)
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.also { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                //TODO handle IOException for timeout???? surely....
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                service.connect(socket)
                service.ping()
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth Client", "Could not close the client socket", e)
            }
        }
    }
}