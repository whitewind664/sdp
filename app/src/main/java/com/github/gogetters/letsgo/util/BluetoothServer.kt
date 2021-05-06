package com.github.gogetters.letsgo.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class BluetoothServer(val handler: Handler) {

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
            service = BluetoothPingService()
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e("BTSERVER", "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {

                    service.connect(it)
                    // Wait for ping
                    var timeout = false
                    val time = Calendar.getInstance().time
                    while (!(service as BluetoothPingService).receivedPing) {
                        val diff = Calendar.getInstance().time - time
                        if (diff - time > 2000) {
                            break
                        }
                    }

                    service.close()

                    if ((service as BluetoothPingService).receivedPing) {
                        service = BluetoothGTPService()
                        service.connect(it)
                        shouldLoop = false
                    } else {
                        shouldLoop = true
                    }
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e("BTSERVER", "Could not close the connect socket", e)
            }
        }
    }
}