package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.util.RemoteService

class RemotePlayerAdapter(private val underlyingPlayer: Player,
                          private val service: RemoteService): Player by underlyingPlayer {


    override fun requestMove(board: BoardState): Move {
        val move = underlyingPlayer.requestMove(board)
        service.notify(move)
        return move
    }
}