package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.InputDelegate


open class LocalPlayer(override val color: Stone, inputDelegate: InputDelegate): Player, InputDelegate by inputDelegate {

    override fun requestMove(board: BoardState): Move {
        return Move(color, getLatestInput(board))
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        TODO("Not yet implemented")
    }

}