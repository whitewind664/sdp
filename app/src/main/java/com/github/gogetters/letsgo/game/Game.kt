package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.utils.CircularList

internal class Game(private val size: Int, val komi: Double,
                    private val whitePlayer: Player, private val blackPlayer: Player) {

    private val board = Board(size)
    private val passMove = Board.Move(Board.Stone.EMPTY, Board.Point(0, 0))
    private var lastMove: Board.Move? = null
    private val players = CircularList(listOf(whitePlayer, blackPlayer))
    private var passes = 0

    fun play() {
        for (player in players) {
            val move = playTurn(player)
            lastMove = move
            if (move == passMove) ++passes

            if (passes == 2) {
                break
            }
        }
    }

    private fun playTurn(player: Player): Board.Move {
        val boardView = board.getView(lastMove, whitePlayer.getPoints(),
                blackPlayer.getPoints())
        val move = player.requestMove(boardView)

        try {
            val points = board.playMove(move)
            player.givePoints(points)

        } catch (e: IllegalMoveException) {
            player.notifyIllegalMove(e)
            return playTurn(player)
        }
        return move
    }
}