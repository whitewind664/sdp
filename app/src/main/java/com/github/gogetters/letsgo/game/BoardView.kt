package com.github.gogetters.letsgo.game

class BoardView(val board: HashMap<Board.Point, Board.Stone>,
                val lastMove: Board.Move?, val koMove: Board.Move?,
                val whiteScore: Int, val blackScore: Int) {

}