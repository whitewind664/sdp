package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.BluetoothInputDelegate
import com.github.gogetters.letsgo.game.util.TouchInputDelegate
import com.github.gogetters.letsgo.util.BluetoothGTPService
import kotlin.IllegalArgumentException

interface Player {

    val color: Stone

    fun requestMove(board: BoardState): Move

    fun notifyIllegalMove(illegalMove: IllegalMoveException)

    companion object {
        fun playerOf(color: Stone, type: String, touchInputDelegate: TouchInputDelegate,
                     bluetoothGTPService: BluetoothGTPService): Player {

            return when(type) {
                "LOCAL" -> LocalPlayer(color, touchInputDelegate)
                "BTREMOTE" -> BluetoothRemotePlayer(color, BluetoothInputDelegate(bluetoothGTPService))
                "BTLOCAL" -> BluetoothLocalPlayer(LocalPlayer(color, touchInputDelegate), bluetoothGTPService)
                else -> throw IllegalArgumentException("INVALID PLAYER TYPE")
            }
        }
    }

    enum class PlayerTypes {
        LOCAL, BTREMOTE, BTLOCAL
    }
}