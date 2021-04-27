package com.github.gogetters.letsgo.game

import android.util.Log
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.util.CircularList

internal open class Game(val size: Board.Size, val komi: Double,
                         private val whitePlayer: Player, private val blackPlayer: Player) {

    private var board = Board(size)
    private val passMove = Move(Stone.EMPTY, Point(0, 0))
    protected var nextPlayer = blackPlayer

    private var passes = 0
    private var whiteScore = 0
    private var blackScore = 0

    /**
     * Called to advance the game state.
     * @return state of the board after the move
     */
    open fun playTurn(): BoardState {
        Log.d("GAME", "${nextPlayer.color}'s turn")

        var validMove = false
        do {
            try {
                val nextMove = nextPlayer.requestMove(board.getBoardState(0, 0))
                val points = board.playMove(nextMove)
                Log.d("GAME", "PLAYED A ${nextMove.stone} STONE AT ${nextMove.coord}")
                addPoints(nextPlayer, points)

                if (nextMove == passMove) ++passes
                else passes = 0

                validMove = true

            } catch (e: IllegalMoveException) {
                nextPlayer.notifyIllegalMove(e)
            }
        } while (!validMove)

        updateNextPlayer(nextPlayer.color)

        return board.getBoardState(0, 0, gameOver = passes >= 2)
    }

    protected fun reinitBoard(playedStones: List<Move>): BoardState {
        board = Board(this.size)
        try {
            for (move in playedStones) {
                board.playMove(move)
            }
            if (playedStones.isNotEmpty()) {
                updateNextPlayer(playedStones[playedStones.size - 1].stone)
            } else {
                nextPlayer = blackPlayer
            }
        } catch (e: IllegalMoveException) {
            board = Board(this.size)
        }
        return board.getBoardState(0, 0)
    }

    private fun updateNextPlayer(lastPlayedColor: Stone) {
        nextPlayer = if (lastPlayedColor == Stone.BLACK) whitePlayer else blackPlayer
    }

    private fun addPoints(player: Player, points: Int) {
        when (player.color)  {
            Stone.WHITE -> whiteScore += points
            Stone.BLACK -> blackScore += points
            else -> return
        }
    }
}