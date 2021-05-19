package com.github.gogetters.letsgo.tutorial

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.view.GoView

class TutorialGoView(context: Context): GoView(context, Board.Size.SMALL) {
    private val whiteStoneTransparentImage = ContextCompat.getDrawable(context, R.drawable.white_transparent)
    private val blackStoneTransparentImage = ContextCompat.getDrawable(context, R.drawable.black_transparent)

    private var playOptionsPerTurn: List<List<Move>> = emptyList()
    private var turnCount: Int = 0

    fun setPlayOptions(playOptions: List<List<Move>>) {
        this.playOptionsPerTurn = playOptions
        turnCount = 0
    }

    override fun getStoneImage(col: Int, row: Int, color: Stone): Drawable? {
        val move = Move(color, Point(col, row))
        return if (getCurrentPlayOptions().contains(move)) {
            when(color) {
                Stone.BLACK -> blackStoneTransparentImage
                else -> whiteStoneTransparentImage
            }
        } else {
            when(color) {
                Stone.BLACK -> blackStoneImage
                else -> whiteStoneImage
            }
        }
    }

    override fun drawStones(canvas: Canvas) {
        super.drawStones(canvas)
        for (move in getCurrentPlayOptions()) {
            super.drawStoneAt(canvas, move.point.first, move.point.second, move.stone)
        }
    }

    override fun updateBoardState(boardState: BoardState) {
        super.updateBoardState(boardState)
        turnCount++
    }

    private fun getCurrentPlayOptions(): List<Move> {
        // play options are only relevant for the local player --> only update every second turn
        val index = (turnCount + 1) / 2
        return if (index < playOptionsPerTurn.size) playOptionsPerTurn[index] else emptyList()
    }
}