package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException

class LocalPlayer(color: Stone): Player(color) {
    override fun requestMove(board: BoardState): Move {
        return Move(color, Point(0, 0))
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        TODO("Not yet implemented")

    }

}