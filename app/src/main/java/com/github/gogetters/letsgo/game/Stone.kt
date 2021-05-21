package com.github.gogetters.letsgo.game

import java.lang.IllegalArgumentException

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

    companion object {
        fun fromString(string: String): Stone {
            return when(string) {
                "black" -> BLACK
                "white" -> WHITE
                "pass" -> EMPTY
                else -> throw IllegalArgumentException("color not recognized")
            }
        }
    }
}
