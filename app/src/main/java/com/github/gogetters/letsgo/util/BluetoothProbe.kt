package com.github.gogetters.letsgo.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.util.*

class BluetoothProbe {

    fun connect(device: BluetoothDevice): String {
        val uuid: UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")
        val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createInsecureRfcommSocketToServiceRecord(uuid)
        }
            // Cancel discovery because it otherwise slows down the connection.
           //bluetoothAdapter?.cancelDiscovery()

            mmSocket?.also { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                //TODO handle IOException for timeout???? surely....
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.


                socket.close()
            }

        //TODO: replace this with the player info as a string
        return "Let's go Player"
    }


}