package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.util.BluetoothGTPService
import kotlin.IllegalArgumentException

interface Player {

    val color: Stone

    fun requestMove(board: BoardState): Move

    fun notifyIllegalMove(illegalMove: IllegalMoveException)

    companion object {
        /**
         * Creates a player according to the type and input
         */
        fun playerOf(color: Stone, type: Int, touchInputDelegate: InputDelegate, bluetoothGTPService: BluetoothGTPService): Player {
            return when(type) {
                PlayerTypes.LOCAL.ordinal -> LocalPlayer(color, touchInputDelegate)
                PlayerTypes.BTREMOTE.ordinal -> LocalPlayer(color, bluetoothGTPService.inputDelegate)
                PlayerTypes.BTLOCAL.ordinal -> BluetoothPlayerAdapter(LocalPlayer(color, touchInputDelegate), bluetoothGTPService)
                else -> throw IllegalArgumentException("INVALID PLAYER TYPE")
            }
        }
    }

    enum class PlayerTypes {
        LOCAL, BTREMOTE, BTLOCAL
    }
}