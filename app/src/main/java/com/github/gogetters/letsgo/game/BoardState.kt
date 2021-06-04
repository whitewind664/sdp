package com.github.gogetters.letsgo.game

data class BoardState(val board: HashMap<Point, Stone>, val koMove: Move?,
                 val whiteScore: Int, val blackScore: Int,
                 val gameOver: Boolean=false, val lastMove: Move?=null) {

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
    }
}

