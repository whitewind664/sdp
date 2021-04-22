package com.github.gogetters.letsgo.tutorial

import com.github.gogetters.letsgo.game.BoardState
import com.github.gogetters.letsgo.game.LocalPlayer
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.exceptions.IllegalMoveException
import com.github.gogetters.letsgo.game.util.InputDelegate

internal class TutorialLocalPlayer(inputDelegate: InputDelegate): LocalPlayer(Stone.BLACK, inputDelegate) {

    private var recommendedMoves: List<Move> = emptyList()

    override fun requestMove(board: BoardState): Move {
        var move: Move
        do {
            move = super.requestMove(board)
            val isGoodChoice = isGoodChoice(move)
            if (!isGoodChoice)
                notifyBadMove("This is not the best move... Think again!")
        } while(isGoodChoice)
        return move
    }

    fun setRecommendedMoves(recommendedMoves: List<Move>) {
        this.recommendedMoves = recommendedMoves
    }

    private fun isGoodChoice(move: Move): Boolean {
        return recommendedMoves.isEmpty() || move in recommendedMoves
    }

    /**
     * Notifies about a move that is not illegal, but just not a good choice in this situation.
     */
    private fun notifyBadMove(comment: String) {
        // TODO notify the user about the bad choice
    }
}