package com.github.gogetters.letsgo.game

class BoardView(val board: HashMap<Point, Stone>,
                val lastMove: Move?, val koMove: Move?,
                val whiteScore: Int, val blackScore: Int)