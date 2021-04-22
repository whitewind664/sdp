package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import java.lang.IllegalArgumentException

interface Player {

    abstract fun requestMove(board: BoardState): Move

    abstract fun notifyIllegalMove(illegalMove: IllegalMoveException)

}