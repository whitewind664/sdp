package com.github.gogetters.letsgo.game.util

import android.os.Handler
import android.os.Looper
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.util.BluetoothGTPService
import java.util.concurrent.ArrayBlockingQueue

/**
 * Class to delegate capturing input from a remote player over Bluetooth.
 */
class BluetoothDelegate(val color: Stone, private val service: BluetoothGTPService): Player {


    override fun requestMove(board: BoardState): Move {
        //service.sendCommand(GTPCommand.PLAY(board.lastMove))
        service.sendCommand(GTPCommand.GENMOVE(color))
        return Move(color, service.moveQueue.take())
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        TODO("Not yet implemented")
    }
}