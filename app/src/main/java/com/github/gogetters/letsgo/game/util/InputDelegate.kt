package com.github.gogetters.letsgo.game.util

import com.github.gogetters.letsgo.game.BoardState
import com.github.gogetters.letsgo.game.Point

interface InputDelegate {

    fun getLatestInput(boardState: BoardState): Point
    fun saveLatestInput(input: Point)
}