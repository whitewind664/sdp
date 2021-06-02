package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.util.BluetoothGTPService
import kotlin.IllegalArgumentException

interface Player {

    val color: Stone

    fun requestMove(board: BoardState): Move

    fun notifyIllegalMove(illegalMove: IllegalMoveException)
}