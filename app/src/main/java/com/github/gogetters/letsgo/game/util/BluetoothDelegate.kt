package com.github.gogetters.letsgo.game.util

import android.os.Handler
import com.github.gogetters.letsgo.game.Point
import java.util.concurrent.ArrayBlockingQueue

/**
 * Class to delegate capturing input from UI.
 */
class BluetoothDelegate: Handler() {

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

    val latestInput: Point
        get() = savedInput.take()
}