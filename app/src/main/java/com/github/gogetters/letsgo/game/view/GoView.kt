package com.github.gogetters.letsgo.game.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.Board
import com.github.gogetters.letsgo.game.BoardState
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.InputDelegate
import kotlin.math.roundToInt


open class GoView(context: Context, private val boardSize: Board.Size) : View(context) {

    constructor(context: Context): this(context, Board.Size.withSize(9))


    private val boardImageID = when (boardSize) {
        Board.Size.SMALL -> R.drawable.board_9
        Board.Size.MEDIUM -> R.drawable.board_13
        Board.Size.LARGE -> R.drawable.board_19
    }

    lateinit var inputDelegate: InputDelegate

    private val boardImage = ContextCompat.getDrawable(context, boardImageID)
    protected val whiteStoneImage = ContextCompat.getDrawable(context, R.drawable.white)
    protected val blackStoneImage = ContextCompat.getDrawable(context, R.drawable.black)

    private var marginRatio = 120F / 880F


    private var board = BoardState.emptyBoard(boardSize)


    private fun coordToPoint(x: Float, y: Float): Point {

        val marginX: Float = width.toFloat() * marginRatio
        val marginY: Float = height.toFloat() * marginRatio

        val maxX = width.toFloat() - 2F * marginX
        val maxY = height.toFloat() - 2F * marginY

        val squareWidth = maxX / (boardSize.size - 1)
        val squareHeight = maxY / (boardSize.size - 1)

        width.toFloat() * marginRatio

        val adjustedX = (x - marginX).coerceAtLeast(0F).coerceAtMost(maxX)
        val adjustedY = (y - marginY).coerceAtLeast(0F).coerceAtMost(maxY)
        

        val col = (adjustedX / squareWidth).roundToInt() + 1
        val row = (adjustedY / squareHeight).roundToInt() + 1
        return Point(col, row)
    }

    private fun pointToCoord(point: Point): Pair<Float, Float> {

        val marginX: Float = width.toFloat() * marginRatio
        val marginY: Float = height.toFloat() * marginRatio

        val maxX = width.toFloat() - 2F * marginX
        val maxY = height.toFloat() - 2F * marginY

        val squareWidth = maxX / (boardSize.size - 1)
        val squareHeight = maxY / (boardSize.size - 1)
        val x = (point.first - 1) * squareWidth + marginX
        val y = (point.second - 1) * squareHeight + marginY

        return Pair(x, y)
    }

    private fun drawBoard(canvas: Canvas) {
        boardImage?.bounds = canvas.clipBounds
        boardImage?.draw(canvas)
    }

    protected fun drawStoneAt(canvas: Canvas, col: Int, row: Int, color: Stone) {
        Log.d("GOVIEW", "DRAWING STONE AT $col, $row")


        val stoneImage = getStoneImage(col, row, color)

        val marginX: Float = width.toFloat() * marginRatio
        val marginY: Float = height.toFloat() * marginRatio

        val maxX = width.toFloat() - 2F * marginX
        val maxY = height.toFloat() - 2F * marginY

        val squareWidth = maxX / (boardSize.size - 1)
        val squareHeight = maxY / (boardSize.size - 1)

        val coord = pointToCoord(Point(col, row))
        val left = (coord.first - squareWidth / 2).roundToInt()
        val top = (coord.second - squareHeight / 2).roundToInt()
        val right = (coord.first + squareWidth / 2).roundToInt()
        val bottom = (coord.second + squareHeight / 2).roundToInt()

        val boundRect = Rect(left, top, right, bottom)
        stoneImage?.bounds = boundRect
        stoneImage?.draw(canvas)

    }

    /**
     * Function that can be overwritten if other stone images want to be used
     */
    protected open fun getStoneImage(col: Int, row: Int, color: Stone): Drawable? {
        return when(color) {
            Stone.BLACK -> blackStoneImage
            else -> whiteStoneImage
        }
    }

    protected open fun drawStones(canvas: Canvas) {
        for (i in 1..boardSize.size) {
            for (j in 1..boardSize.size) {
                val color = board[Point(i, j)]
                if (color != Stone.EMPTY && color != null) {
                    drawStoneAt(canvas, i, j, color)
                }
            }
        }
    }

    open fun updateBoardState(boardState: BoardState) {
        Log.d("GOVIEW", "UPDATING BOARD STATE: \n" +
                "${boardState.board}")
        this.board = boardState.board
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        drawBoard(canvas)
        drawStones(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val closestPoint = coordToPoint(x, y)
                inputDelegate.saveLatestInput(closestPoint)
            }
        }

        return true
    }
}