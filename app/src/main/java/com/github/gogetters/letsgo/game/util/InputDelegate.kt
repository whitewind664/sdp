package com.github.gogetters.letsgo.game.util

import com.github.gogetters.letsgo.game.Point
import java.util.concurrent.ArrayBlockingQueue

class InputDelegate {

    private val savedInput = ArrayBlockingQueue<Point>(1)

    fun getLatestInput(): Point = savedInput.take()

    /**
     * Saves the argument as the most recent input to the program. Only the most recent input is
     * kept.
     *
     * @param input: Point input to save
     */
    fun saveLatestInput(input: Point) {
        savedInput.clear()
        savedInput.add(input)
    }

    fun clearInput() {
        savedInput.clear()
    }

    fun hasInput(): Boolean {
        return !savedInput.isEmpty()
    }
}