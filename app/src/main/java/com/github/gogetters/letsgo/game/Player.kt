package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import java.lang.IllegalArgumentException

abstract class Player(val color: Board.Stone) {
    private var points = 0
    init {
        if (color == Board.Stone.EMPTY)
            throw IllegalArgumentException("Player's color cannot be empty")
    }

    abstract fun requestMove(board: BoardView)

    abstract fun notifyIllegalMove(illegalMove: IllegalMoveException)

    fun givePoints(newPoints: Int) {
        points += newPoints
    }

    fun getPoints(): Int = points
}