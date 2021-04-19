package com.github.gogetters.letsgo.tutorial

import com.github.gogetters.letsgo.game.BoardState
import com.github.gogetters.letsgo.game.LocalPlayer
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.InputDelegate

class TutorialLocalPlayer(inputDelegate: InputDelegate): LocalPlayer(Stone.BLACK, inputDelegate) {

    private var turnCount: Int = 0

    override fun requestMove(board: BoardState): Move {
        var move: Move
        do {
            move = super.requestMove(board)
            val isGoodChoice = false
            if (!isGoodChoice)
                notifyBadMove("This is not the best move... Think again!")
        } while(isGoodChoice)
        return move
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        super.notifyIllegalMove(illegalMove)
    }

    private fun notifyBadMove(comment: String) {
        // TODO notify the user about the bad choice
    }
}