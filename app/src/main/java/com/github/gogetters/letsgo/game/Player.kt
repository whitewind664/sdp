package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import java.lang.IllegalArgumentException

interface Player {

    val color: Stone

    fun requestMove(board: BoardState): Move

    fun notifyIllegalMove(illegalMove: IllegalMoveException)

}