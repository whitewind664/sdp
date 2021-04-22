package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.BluetoothInputDelegate
import com.github.gogetters.letsgo.game.util.InputDelegate

class BluetoothPlayer(override val color: Stone, private val bluetoothInputDelegate: BluetoothInputDelegate):
        Player, InputDelegate by bluetoothInputDelegate {

    override fun requestMove(board: BoardState): Move {
        val point = getLatestInput(board)
        return Move(color, point)
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        TODO("Not yet implemented, maybe add something to bluetoothInputDelegate??")
    }

}