package com.github.gogetters.letsgo.game

class BoardState(val board: HashMap<Point, Stone>, val koMove: Move?,
                 val whiteScore: Int, val blackScore: Int, val gameOver: Boolean=false) {

    companion object {
        fun emptyBoard(size: Int): HashMap<Point, Stone> {

            val board = HashMap<Point, Stone>()
            for (i in 1..size) {
                for (j in 1..size) {
                    board[Point(i, j)] = Stone.EMPTY
                }
            }

            return board
        }
    }
}

