package com.github.gogetters.letsgo.game.util

import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.util.BluetoothGTPService
import java.util.concurrent.ArrayBlockingQueue

/**
 * Class to delegate capturing input from a remote player over Bluetooth.
 */
class BluetoothInputDelegate(private val service: BluetoothGTPService): InputDelegate {

    private val moveQueue = ArrayBlockingQueue<Point>(1)

    override fun getLatestInput(boardState: BoardState): Point {
        val lastMove = boardState.lastMove!!

        service.sendCommand(GTPCommand.PLAY(lastMove))
        service.sendCommand(GTPCommand.GENMOVE(Stone.WHITE))
        return moveQueue.take()
    }

    override fun saveLatestInput(input: Point) {
        moveQueue.clear()
        moveQueue.add(input)
    }
}