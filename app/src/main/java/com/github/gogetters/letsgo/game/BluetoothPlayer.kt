package com.github.gogetters.letsgo.game

import android.util.Log
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.BluetoothDelegate

class BluetoothPlayer(color: Stone, private val bluetoothDelegate: BluetoothDelegate): Player(color) {

    override fun requestMove(board: BoardState): Move {
        return Move(color, bluetoothDelegate.latestInput)
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        Log.d("LOCAL_PLAYER", "PLAYER HAS PLAYED ILLEGAL MOVE", illegalMove)

    }

}