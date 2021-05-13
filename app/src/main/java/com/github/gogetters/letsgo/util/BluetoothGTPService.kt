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


    fun sendCommand(gtpCommand: GTPCommand) {
        val serializedCommand = gtpCommand.toString()
        val commandBytes = serializedCommand.toByteArray()
        write(commandBytes)
    }

    private fun parseCommand(bytes: ByteArray): Boolean {
        try {
            val commandString = if (bytes.indexOf(0) == -1) {
                String(bytes)
            } else {
                bytes.decodeToString(0, bytes.indexOf(0))
            }

            Log.d("BLUETOOTHGTPSERVICE", "RECEIVED COMMAND $commandString")
            if (commandString == PING) {
                receivedPing = true
                sendCommand(GTPCommand.CLEAR_BOARD)
                return true
            }

            when (val command = GTPCommand.toCommand(commandString)) {
                is GTPCommand.PLAY -> inputDelegate.saveLatestInput(command.move.point)
                is GTPCommand.GENMOVE -> return true
                else -> return false
            }

        } catch (e: Error) { //TODO figure out what exception bytes.toString() would throw
            return false
        } catch (e: Error) {
            return false
        }
        return true
    }
}