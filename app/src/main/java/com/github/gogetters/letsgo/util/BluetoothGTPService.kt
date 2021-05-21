package com.github.gogetters.letsgo.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.gogetters.letsgo.game.GTPCommand
import com.github.gogetters.letsgo.game.util.InputDelegate


class BluetoothGTPService: BluetoothService() {

    override val handler = Handler(Looper.getMainLooper()) {
        if (it.what == MESSAGE_READ) {
            parseCommand(it.obj as ByteArray)
        } else {
            true
        }
    }

    lateinit var inputDelegate: InputDelegate


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
                Log.d("BLUETOOTHGTPSERVICE", "CONNECTED")
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