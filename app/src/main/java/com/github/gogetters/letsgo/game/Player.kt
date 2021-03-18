package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import java.lang.IllegalArgumentException

abstract class Player(val color: Stone) {
    init {
        if (color == Stone.EMPTY)
            throw IllegalArgumentException("Player's color cannot be empty")
    }

    abstract fun requestMove(board: BoardState): Move

    abstract fun notifyIllegalMove(illegalMove: IllegalMoveException)

}