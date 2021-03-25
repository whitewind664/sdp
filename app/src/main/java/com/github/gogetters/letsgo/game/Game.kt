package com.github.gogetters.letsgo.game

import android.util.Log
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.util.CircularList

internal class Game(size: Board.Size, val komi: Double,
                    private val whitePlayer: Player, private val blackPlayer: Player) {

    private val board = Board(size)
    private val passMove = Move(Stone.EMPTY, Point(0, 0))
    private var lastMove: Move? = null
    private var nextPlayer = blackPlayer

    private var passes = 0
    private var whiteScore = 0
    private var blackScore = 0

    /**
     * Called to advance the game state.
     * @return state of the board after the move
     */
    fun playTurn(): BoardState {
        Log.d("GAME", "${nextPlayer.color}'s turn")

        var validMove = false
        do {
            try {
                val boardState = board.getBoardState(0, 0, lastMove)
                val nextMove = nextPlayer.requestMove(boardState)
                val points = board.playMove(nextMove)
                Log.d("GAME", "PLAYED A ${nextMove.stone} STONE AT ${nextMove.coord}")
                addPoints(nextPlayer, points)

                if (nextMove == passMove) ++passes
                else passes = 0

                validMove = true
                lastMove = nextMove

            } catch (e: IllegalMoveException) {
                nextPlayer.notifyIllegalMove(e)
            }
        } while (!validMove)

        nextPlayer = if (nextPlayer.color == Stone.BLACK) whitePlayer else blackPlayer

        return board.getBoardState(0, 0,
            lastMove = lastMove, gameOver = passes >= 2)
    }

    private fun addPoints(player: Player, points: Int) {
        when (player.color)  {
            Stone.WHITE -> whiteScore += points
            Stone.BLACK -> blackScore += points
            else -> return
        }
    }
}