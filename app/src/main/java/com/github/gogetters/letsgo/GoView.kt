package com.github.gogetters.letsgo

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.github.gogetters.letsgo.game.Board
import com.github.gogetters.letsgo.game.Game
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone


class GoView(context: Context?) : View(context) {

    private final val originX = 10f
    private final val originY = 200f
    private final val cellSide = 110f
    private final val paint = Paint()
    private final val ids = setOf(R.drawable.white, R.drawable.black)
    private final val bitmaps = mutableMapOf<Int, Bitmap>()

    init {
        load()
    }

    override fun onDraw(canvas: Canvas?) {
        drawBoard(canvas)
        drawStones(canvas)
    }

    private fun load() {
        for (id in ids) {
            bitmaps[id] = BitmapFactory.decodeResource(resources, id)
        }
    }

    private fun drawBoard(canvas: Canvas?) {
        // example for 13x13

        // border
        paint.style = Paint.Style.FILL;
        paint.color = resources.getColor(R.color.board, resources.newTheme())
        for (row in 0 until 9)
            for (col in 0 until 9)
                drawRec(canvas, col, row)

        paint.style = Paint.Style.STROKE;
        paint.strokeWidth = 1f
        paint.color = Color.BLACK;
        for (row in 0 until 9)
            for (col in 0 until 9)
                drawRec(canvas, col, row)
    }

    private fun drawRec(canvas: Canvas?, col: Int, row: Int) {
        canvas?.drawRect(originX + col * cellSide, originY + row * cellSide, originX + (col + 1)* cellSide, originY + (row + 1) * cellSide, paint)
    }

    private fun drawStoneAt(canvas: Canvas?, col: Int, row: Int, id: Int) {
        val bitmap = bitmaps[id]!!
        canvas?.drawBitmap(bitmap, null, RectF(originX + (col + 0.5f) * cellSide, originY + (row + 0.5f) * cellSide, originX + (col + 1.5f) * cellSide, originY + (row + 1.5f) * cellSide), paint)
    }

    private fun drawStones(canvas: Canvas?) {
        // reset board?
        // get access to GAME board -> to see whether white, black or empty are the coordinates
        for (row in 0..7)
            for (col in 0..7) {
                // TODO!!! connection with model
                //val stone = getBoard() from the game
                //if (stone == Stone.WHITE) drawStoneAt(canvas, col, row, R.drawable.white)
                //if (stone == Stone.BLACK) drawStoneAt(canvas, col, row, R.drawable.black)
                if (col == 0 && row == 0) drawStoneAt(canvas, col, row, R.drawable.white)
                if (col == 7 && row == 0) drawStoneAt(canvas, col, row, R.drawable.black)
            }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {

            }
        }
        return true
    }

}