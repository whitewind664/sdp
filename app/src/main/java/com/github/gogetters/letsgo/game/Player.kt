package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import java.lang.IllegalArgumentException

abstract class Player(val color: Stone) {
    private var points = 0
    init {
        if (color == Stone.EMPTY)
            throw IllegalArgumentException("Player's color cannot be empty")
    }

    abstract fun requestMove(board: BoardView): Move

    abstract fun notifyIllegalMove(illegalMove: IllegalMoveException)

    //TODO this is dumb
    fun givePoints(newPoints: Int) {
        points += newPoints
    }

    fun getPoints(): Int = points
}