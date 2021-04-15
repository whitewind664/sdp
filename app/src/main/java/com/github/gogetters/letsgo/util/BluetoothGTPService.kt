package com.github.gogetters.letsgo.util

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.gogetters.letsgo.game.GTPCommand
import com.github.gogetters.letsgo.game.Move
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ArrayBlockingQueue


class BluetoothGTPService(private val handler: Handler) {
    private val TAG = "MY_APP_DEBUG_TAG"

    private val MESSAGE_READ: Int = 0
    private val MESSAGE_WRITE: Int = 1
    private val MESSAGE_TOAST: Int = 2


    private val queue = ArrayBlockingQueue<Move>(64)

    private lateinit var connectedThread: ConnectedThread

    fun connect(socket: BluetoothSocket) {
        connectedThread = ConnectedThread(socket)
        connectedThread.start()
        write("HELLO WORLD")
    }

    fun write(s: String) {
        val bytes = s.toByteArray()
        connectedThread.write(bytes)
    }

    fun sendCommand(gtpCommand: GTPCommand) {
        val serializedCommand = gtpCommand.toString()
        val commandBytes = serializedCommand.toByteArray()
        connectedThread.write(commandBytes)
    }

    private fun receiveCommand(bytes: ByteArray): Boolean {
        try {
            val commandString = bytes.toString()
            val command = GTPCommand.toCommand(commandString)

            if (command is GTPCommand.PLAY) {
                queue.add(command.move)
            }

        } catch (e: Error) { //TODO figure out what exception bytes.toString() would throw
            return false
        } catch (e: Error) {
            return false
        }
        return true
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
                        mmBuffer)
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
                    MESSAGE_WRITE, -1, -1, bytes)
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