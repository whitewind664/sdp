package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException

class BotPlayer(private val engine: Engine, color: Stone): Player(color) {

    init {
        engine.initialize()
    }

    override fun requestMove(board: BoardState): Move {
        engine.enemyPlay()
        return engine.requestMove()
    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        TODO("Not yet implemented")
    }
}