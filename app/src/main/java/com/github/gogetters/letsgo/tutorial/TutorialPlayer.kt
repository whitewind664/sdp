package com.github.gogetters.letsgo.tutorial

import android.util.Log
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException

internal
class TutorialPlayer(override val color: Stone = Stone.WHITE): Player {
    private val DEFAULT_MOVE = Move(Stone.EMPTY, Point(0, 0))

    private var moveIndex: Int = 0
    private var moveCoordinates: List<Point> = emptyList()

    override fun requestMove(board: BoardState): Move {
        return if (this.isOutOfMoves()) {
            moveIndex++
            DEFAULT_MOVE
        } else {
            Move(Stone.WHITE, this.moveCoordinates[moveIndex++])
        }
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        Log.d("TUTORIAL_PLAYER", "PLAYER HAS PLAYED ILLEGAL MOVE", illegalMove)
        // TODO handle case that tutorial player plays illegal move
    }

    fun setMoves(moves: List<Point>) {
        moveIndex = 0
        if (moves.isNotEmpty()) {
            this.moveCoordinates = moves
        } else {
            this.moveCoordinates = emptyList()
        }
    }

    fun isOutOfMoves(): Boolean {
        return moveIndex >= moveCoordinates.size
    }

}