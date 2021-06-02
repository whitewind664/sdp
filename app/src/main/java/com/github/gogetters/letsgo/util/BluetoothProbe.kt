package com.github.gogetters.letsgo.util


import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.provider.Settings.Global.getString
import android.util.Log
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.user.FirebaseUserBundle
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException
import java.util.*

class BluetoothProbe {

    fun connect(device: BluetoothDevice, service: BluetoothService): String {
        val uuid: UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")
        val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createInsecureRfcommSocketToServiceRecord(uuid)
        }

        var nick = "Anonymous Go Player"
            // Cancel discovery because it otherwise slows down the connection.
           //bluetoothAdapter?.cancelDiscovery()

            mmSocket?.also { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                //TODO handle IOException for timeout???? surely....
                socket.connect()
                val mmInStream = socket.inputStream
                val mmBuffer = ByteArray(1024)
                var numBytes = 0

                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d("BTProbe", "Input stream was disconnected", e)
                }

                nick = String(mmBuffer, 0, numBytes)

                socket.close()
            }

        return nick
    }
}