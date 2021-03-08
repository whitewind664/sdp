package com.github.gogetters.letsgo.game

import java.lang.IllegalArgumentException

internal open class Player(val color: Stone) {
    init {
        if (color == Stone.EMPTY) throw IllegalArgumentException("Player's color cannot be empty")
    }
}