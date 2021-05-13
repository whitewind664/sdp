package com.github.gogetters.letsgo.game.util

import android.util.Log
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import java.util.concurrent.ArrayBlockingQueue

/**
 * Class to delegate capturing input from UI.
 */
class TouchInputDelegate: InputDelegate {

    private val savedInput = ArrayBlockingQueue<Point>(1)

    override fun getLatestInput(boardState: BoardState): Point = savedInput.take()

    /**
     * Saves the argument as the most recent input to the program. Only the most recent input is
     * kept.
     *
     * @param input: Point input to save
     */
    override fun saveLatestInput(input: Point) {
        savedInput.clear()
        savedInput.add(input)
    }

    fun clearInput() {
        savedInput.clear()
    }

}