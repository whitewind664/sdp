package com.github.gogetters.letsgo.util

import com.github.gogetters.letsgo.game.GTPCommand
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.game.util.RemoteService
import com.github.gogetters.letsgo.util.BluetoothService.Companion.PING


class BluetoothGTPService(underlying: BluetoothService): BluetoothService by underlying, RemoteService {

    override var receivedPing = false
    override lateinit var inputDelegate: InputDelegate

    init {
        underlying.handleInput = {bytes -> parseCommand(bytes)}
    }

    override fun notify(move: Move) {
        sendCommand(GTPCommand.PLAY(move))
    }


    private fun sendCommand(gtpCommand: GTPCommand) {
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