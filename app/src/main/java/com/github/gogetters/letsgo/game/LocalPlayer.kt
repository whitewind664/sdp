package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException

class LocalPlayer(color: Stone): Player(color) {
    override fun requestMove(board: BoardView): Move {
        return Move(Stone.BLACK, Point(1,1))
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        TODO("Not yet implemented")

    }

}