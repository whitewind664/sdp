package com.github.gogetters.letsgo.tutorial

import android.util.Log
import com.github.gogetters.letsgo.game.BoardState
import com.github.gogetters.letsgo.game.LocalPlayer
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.InputDelegate

internal class TutorialLocalPlayer(inputDelegate: InputDelegate): LocalPlayer(Stone.BLACK, inputDelegate) {

    /**
     *  Contains the recommended moves per turn of the local player
     */
    private var recommendedMoves: List<List<Move>> = emptyList()
    private var turnCount = 0

    override fun requestMove(board: BoardState): Move {
        var move: Move
        do {
            move = super.requestMove(board)
            val isGoodChoice = isGoodChoice(move)
            if (!isGoodChoice)
                notifyBadMove("This is not the best move... Think again!")
        } while(!isGoodChoice)
        turnCount++
        return move
    }

    fun setRecommendedMoves(recommendedMoves: List<List<Move>>) {
        this.recommendedMoves = recommendedMoves
    }

    private fun isGoodChoice(move: Move): Boolean {
        val recommendedMovesInTurn = if (turnCount < recommendedMoves.size) recommendedMoves[turnCount] else emptyList()
        return recommendedMovesInTurn.isEmpty() || move in recommendedMovesInTurn
    }

    /**
     * Notifies about a move that is not illegal, but just not a good choice in this situation.
     */
    private fun notifyBadMove(comment: String) {
        Log.i("TutorialLocalPlayer", "This is not a good choice")
    }
}