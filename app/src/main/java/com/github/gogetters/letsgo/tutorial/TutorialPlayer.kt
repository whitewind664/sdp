package com.github.gogetters.letsgo.tutorial

import android.util.Log
import com.github.gogetters.letsgo.game.BoardState
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Player
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException

class TutorialPlayer(color: Stone): Player(color) {

    private var turnCount: Int = 0

    override fun requestMove(board: BoardState): Move {
        TODO("Not yet implemented: Implement logic of tutorial player")
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        Log.d("TUTORIAL_PLAYER", "PLAYER HAS PLAYED ILLEGAL MOVE", illegalMove)
        // TODO show to user that this is not the best option
    }

    fun isGoodReactionMove(turnCount: Int, move: Move): Boolean {
        TODO("Not yet implemented : needs this to be static?")
        return true
    }


}