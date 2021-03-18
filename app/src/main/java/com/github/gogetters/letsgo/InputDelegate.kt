package com.github.gogetters.letsgo

import com.github.gogetters.letsgo.game.Point
import java.util.concurrent.ArrayBlockingQueue

class InputDelegate {

    private val savedInput = ArrayBlockingQueue<Point>(1)

    fun saveInput(input: Point) {
        savedInput.clear()
        savedInput.add(input)
    }

    val latestInput: Point
        get() = savedInput.take()
}