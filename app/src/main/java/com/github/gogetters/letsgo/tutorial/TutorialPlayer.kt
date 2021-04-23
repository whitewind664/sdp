package com.github.gogetters.letsgo.tutorial

import android.util.Log
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException

internal class TutorialPlayer(color: Stone): Player(color) {

    private var turnCount: Int = 1

    override fun requestMove(board: BoardState): Move {
        //TODO("Not yet implemented: Implement logic of tutorial player")
        turnCount++
        return Move(color, Point(5, turnCount))
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        Log.d("TUTORIAL_PLAYER", "PLAYER HAS PLAYED ILLEGAL MOVE", illegalMove)
        // TODO show to user that this is not the best option
    }

}