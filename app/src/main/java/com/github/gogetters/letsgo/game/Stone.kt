package com.github.gogetters.letsgo.game

enum class Stone {
    BLACK, WHITE, EMPTY;

    fun otherColor(): Stone {
        if (this == EMPTY) return EMPTY
        return if (this == BLACK) WHITE else BLACK
    }
}
