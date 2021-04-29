package com.github.gogetters.letsgo.game.util

import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.util.BluetoothGTPService
import java.util.concurrent.ArrayBlockingQueue

/**
 * Class to delegate capturing input from a remote player over Bluetooth.
 */
class BluetoothInputDelegate(private val service: BluetoothGTPService): InputDelegate {

    private val moveQueue = ArrayBlockingQueue<Point>(1)

    init {
        service.inputDelegate = this
    }

    override fun getLatestInput(boardState: BoardState): Point {
        return moveQueue.take()
    }

    override fun saveLatestInput(input: Point) {
        moveQueue.clear()
        moveQueue.add(input)
    }
}