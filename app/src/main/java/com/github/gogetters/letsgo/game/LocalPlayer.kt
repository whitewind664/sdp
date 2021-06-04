package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.InputDelegate

open class LocalPlayer(override val color: Stone, private val inputDelegate: InputDelegate): Player {

    override fun requestMove(board: BoardState): Move {
        val point = inputDelegate.getLatestInput(board)
        return if (point == Game.PASS_MOVE.point) {
            Game.PASS_MOVE
        } else {
            Move(color, point)
        }
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        return
    }

}