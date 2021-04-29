package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.util.BluetoothGTPService

class BluetoothLocalPlayer(private val underlyingPlayer: Player,
                           private val service: BluetoothGTPService): Player by underlyingPlayer {

    private fun notifyRemote(move: Move) {
        service.sendCommand(GTPCommand.PLAY(move))
    }

    override fun requestMove(board: BoardState): Move {
        val move = underlyingPlayer.requestMove(board)
        notifyRemote(move)
        return move
    }
}