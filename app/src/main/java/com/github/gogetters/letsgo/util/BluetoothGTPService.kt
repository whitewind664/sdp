package com.github.gogetters.letsgo.util

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.gogetters.letsgo.game.GTPCommand
import com.github.gogetters.letsgo.game.util.BluetoothInputDelegate
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class BluetoothGTPService: BluetoothService() {

    override val handler = Handler(Looper.getMainLooper()) {
        parseCommand(it.obj as ByteArray)
    }

    lateinit var inputDelegate: BluetoothInputDelegate

    private val MESSAGE_READ: Int = 0
    private val MESSAGE_WRITE: Int = 1
    private val MESSAGE_TOAST: Int = 2

    private val TAG = "MY_APP_DEBUG_TAG"


    fun sendCommand(gtpCommand: GTPCommand) {
        val serializedCommand = gtpCommand.toString()
        val commandBytes = serializedCommand.toByteArray()
        write(commandBytes)
    }

    private fun parseCommand(bytes: ByteArray): Boolean {
        try {
            val commandString = String(bytes, charset("utf-8"))
            receivedPing = commandString == PING || receivedPing
            if (commandString == PING) {
                receivedPing = true
                return true
            }

            val command = GTPCommand.toCommand(commandString)

            if (command is GTPCommand.PLAY) {
                inputDelegate.saveLatestInput(command.move.point)
            } else if (command is GTPCommand.GENMOVE) {
                //TODO figure out what to do here??? not sure
            }

        } catch (e: Error) { //TODO figure out what exception bytes.toString() would throw
            return false
        } catch (e: Error) {
            return false
        }
        return true
    }
}