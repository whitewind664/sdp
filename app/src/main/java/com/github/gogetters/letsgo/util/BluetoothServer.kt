package com.github.gogetters.letsgo.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothServer {

    private val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val uuid: UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")

    private val acceptThread = AcceptThread()
    private lateinit var service: BluetoothService

    fun connect(service: BluetoothGTPService) {
        this.service = service
        acceptThread.start()
    }

    fun cancel() {
        acceptThread.cancel()
    }

    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            adapter.listenUsingInsecureRfcommWithServiceRecord("Let's Go Server", uuid)
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()

                } catch (e: IOException) {
                    Log.e("Bluetooth Server", "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    service.connect(it)

                    service.sendNick()


                    // Wait for ping
                    val time = Calendar.getInstance().time
                    while (!service.receivedPing) {
                        val diff = Calendar.getInstance().time.time - time.time
                        if (diff > 2000) {
                            break
                        }
                    }

                    shouldLoop = !service.receivedPing
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth Server", "Could not close the connect socket", e)
            }
        }
    }
}