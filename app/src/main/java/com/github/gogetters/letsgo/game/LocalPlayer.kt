package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException

class LocalPlayer(color: Board.Stone): Player(color) {
    override fun requestMove(board: BoardView): Board.Move {
        TODO("Not yet implemented")
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        TODO("Not yet implemented")
    }

}