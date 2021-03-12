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
     * Initialises the game state and requests the first move from black.
     */
    fun start() {
        val nextPlayer = playersIt.next()
        nextPlayer.requestMove(board.getView(0, 0))
    }


    /**
     * Called by a Player to advance the game state.
     * @param player player who is playing the move
     * @param move move to play
     */
    fun playTurn(player: Player, move: Move) {
        var nextPlayer = playersIt.next()

        try {
            val points = board.playMove(move)
            player.givePoints(points)
            lastMove = move

        } catch (e: IllegalMoveException) {
            player.notifyIllegalMove(e)
            nextPlayer = player
        }

        val boardView = board.getView(whitePlayer.getPoints(),
                blackPlayer.getPoints())
        nextPlayer.requestMove(boardView)
    }
}