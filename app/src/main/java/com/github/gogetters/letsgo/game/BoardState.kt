package com.github.gogetters.letsgo.game

class BoardState(val board: HashMap<Point, Stone>,
                 val whiteScore: Int=0, val blackScore: Int=0,
                 val lastMove: Move?=null,
                 val gameOver: Boolean=false) {

    companion object {
        fun emptyBoard(size: Board.Size): HashMap<Point, Stone> {

            val board = HashMap<Point, Stone>()
            for (i in 1..size.size) {
                for (j in 1..size.size) {
                    board[Point(i, j)] = Stone.EMPTY
                }
            }

            return board
        }

        fun fromEmpty(size: Board.Size): BoardState {
            return BoardState(emptyBoard(size))
        }
    }
}

