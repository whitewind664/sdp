package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException

class LocalPlayer(color: Stone): Player(color) {
    override fun requestMove(board: BoardView) {

    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        TODO("Not yet implemented")

    }

}