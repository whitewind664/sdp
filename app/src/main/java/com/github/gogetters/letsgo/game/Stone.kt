package com.github.gogetters.letsgo.game

enum class Stone {
    BLACK {
        override fun toString(): String {
            return "black"
        }
          },
    WHITE {
        override fun toString(): String {
            return "white"
        }
          },

    EMPTY {
        override fun toString(): String {
            return "pass"
        }
    };

    fun otherColor(): Stone {
        if (this == EMPTY) return EMPTY
        return if (this == BLACK) WHITE else BLACK
    }

    abstract override fun toString(): String
}
