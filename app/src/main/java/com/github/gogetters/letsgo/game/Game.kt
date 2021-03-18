package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.utils.CircularList

internal class Game(size: Board.Size, val komi: Double,
                    private val whitePlayer: Player, private val blackPlayer: Player) {

    private val board = Board(size)
    private val passMove = Move(Stone.EMPTY, Point(0, 0))
    private val players = CircularList(listOf(whitePlayer, blackPlayer))
    private val playersIt = players.iterator()
    private var passes = 0
    private var whiteScore = 0
    private var blackScore = 0

    /**
     * Called to advance the game state.
     * @return state of the board after the move
     */
    fun playTurn(): BoardState {
        val nextPlayer = playersIt.next()

        var validMove = false
        do {
            try {
                val nextMove = nextPlayer.requestMove(board.getView(0, 0))
                val points = board.playMove(nextMove)
                addPoints(nextPlayer, points)

                if (nextMove == passMove) ++passes
                else passes = 0

                validMove = true

            } catch (e: IllegalMoveException) {
                nextPlayer.notifyIllegalMove(e)
            }
        } while (!validMove)

        return board.getView(0, 0, gameOver = passes >= 2)
    }

    private fun addPoints(player: Player, points: Int) {
        when (player.color)  {
            Stone.WHITE -> whiteScore += points
            Stone.BLACK -> blackScore += points
            else -> return
        }
    }
}