package com.github.gogetters.letsgo.util

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.gogetters.letsgo.util.BluetoothService.Companion.MESSAGE_READ
import com.github.gogetters.letsgo.util.BluetoothService.Companion.MESSAGE_TOAST
import com.github.gogetters.letsgo.util.BluetoothService.Companion.MESSAGE_WRITE
import com.github.gogetters.letsgo.util.BluetoothService.Companion.TAG
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class RealBluetoothService: BluetoothService {

    var handler = Handler(Looper.getMainLooper()) {
        if (it.what == MESSAGE_READ) {
            handleInput(it.obj as ByteArray)
        } else {
            true
        }
    }
    override var handleInput: (ByteArray) -> Boolean = {true}

    override var receivedPing = false

    private lateinit var connectedThread: ConnectedThread

    override fun connect(socket: BluetoothSocket) {
        connectedThread = ConnectedThread(socket)
        connectedThread.start()
    }

    override fun close() {
        connectedThread.cancel()
    }

    override fun write(bytes: ByteArray) {
        connectedThread.write(bytes)
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    mmBuffer
                )
                readMsg.sendToTarget()
            }
        }



        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, bytes
            )
            writtenMsg.sendToTarget()
        }



        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }
}