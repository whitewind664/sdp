package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.utils.CircularList

internal class Game(size: Board.Size, val komi: Double,
                    private val whitePlayer: Player, private val blackPlayer: Player) {

    private val board = Board(size)
    private val passMove = Move(Stone.EMPTY, Point(0, 0))
    private var lastMove: Move? = null
    private val players = CircularList(listOf(whitePlayer, blackPlayer))
    private val playersIt = players.iterator()
    private var passes = 0


    /**
     * Called to advance the game state.
     * @return state of the board after the move
     */
    fun playTurn(): BoardView {
        val nextPlayer = playersIt.next()
        //TODO(figure out where to track the score)
        val nextMove = nextPlayer.requestMove(board.getView(0, 0))

        try {
            val points = board.playMove(nextMove)
            nextPlayer.givePoints(points)
            lastMove = nextMove

        } catch (e: IllegalMoveException) {
            nextPlayer.notifyIllegalMove(e)
            //TODO ask the player again for a move... do..while loop?
        }

        return board.getView(0, 0)
    }
}