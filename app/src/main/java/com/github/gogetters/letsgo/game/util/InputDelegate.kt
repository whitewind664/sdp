package com.github.gogetters.letsgo.game.util

import android.util.Log
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import java.util.concurrent.ArrayBlockingQueue

/**
 * Class to delegate capturing input from UI.
 */
class InputDelegate(val color: Stone): Player {

    private val savedInput = ArrayBlockingQueue<Point>(1)

    /**
     * Saves the argument as the most recent input to the program. Only the most recent input is
     * kept.
     *
     * @param input: Point input to save
     */
    fun saveInput(input: Point) {
        savedInput.clear()
        savedInput.add(input)
    }

    override fun requestMove(board: BoardState): Move {
        return Move(color, savedInput.take())
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        Log.d("LOCAL_PLAYER", "PLAYER HAS PLAYED ILLEGAL MOVE", illegalMove)

    }
}