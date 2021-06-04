package com.github.gogetters.letsgo.util.bluetooth.mocking

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import com.github.gogetters.letsgo.game.GTPCommand
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.util.BluetoothService
import com.github.gogetters.letsgo.util.BluetoothService.Companion.PING

class MockBluetoothService: BluetoothService {
    override var handleInput: (ByteArray) -> Boolean = {true}

    override var receivedPing: Boolean = false
    var sentPing = false

    override fun write(bytes: ByteArray) {
        val commandString = if (bytes.indexOf(0) == -1) {
            String(bytes)
        } else {
            bytes.decodeToString(0, bytes.indexOf(0))
        }

        if (commandString == PING) {
            sentPing = true
        }
    }

    override fun connect(socket: BluetoothSocket) {
    }

    override fun close() {
    }

    fun receivePing() {
        handleInput(PING.toByteArray())
    }

    fun receivePlay() {
        handleInput(GTPCommand.PLAY(Move(Stone.BLACK, Point(1, 1))).toString().toByteArray())
    }

}