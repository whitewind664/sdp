package com.github.gogetters.letsgo.tutorial

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.Board
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.view.GoView

class TutorialGoView(context: Context): GoView(context, Board.Size.SMALL) {
    private val whiteStoneTransparentImage = ContextCompat.getDrawable(context, R.drawable.white_transparent)
    private val blackStoneTransparentImage = ContextCompat.getDrawable(context, R.drawable.black_transparent)

    private var playOptions: List<Move> = emptyList()

    fun setPlayOptions(playOptions: List<Move>) {
        this.playOptions = playOptions
    }

    override fun getStoneImage(col: Int, row: Int, color: Stone): Drawable? {
        val move = Move(color, Point(col, row))
        return if (playOptions.contains(move)) {
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
        for (move in playOptions) {
            super.drawStoneAt(canvas, move.coord.first, move.coord.second, move.stone)
        }
    }
}