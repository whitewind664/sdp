package com.github.gogetters.letsgo.game

class BoardState(val board: HashMap<Point, Stone>, val koMove: Move?,
                 val whiteScore: Int, val blackScore: Int, val gameOver: Boolean=false)