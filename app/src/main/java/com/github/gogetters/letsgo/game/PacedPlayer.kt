package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import kotlin.system.measureTimeMillis

final class PacedPlayer(private val underlyingPlayer: Player, private val minTime: Long = 3000, color: Stone): Player(color) {


    /**
     * Lets the underlying player decide Which move to play, if the minimal time is not attained, the thread will sleep until the desired minTime is reached
     */
    override fun requestMove(board: BoardState): Move {

        val initialTime = System.currentTimeMillis()
        val move = underlyingPlayer.requestMove(board)

        val difference = System.currentTimeMillis() - initialTime

        if(difference < minTime){
            try {
                Thread.sleep(minTime - difference)
            } catch(e: InterruptedException){
                //ignore
            }
        }

        return move;

    }

    override fun notifyIllegalMove(illegalMove: IllegalMoveException) {
        underlyingPlayer.notifyIllegalMove(illegalMove)
    }
}