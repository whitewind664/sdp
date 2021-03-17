package com.github.gogetters.letsgo.game

enum class Command {
    PLAY{
        override fun serialize(): String {
            return "play"
        }

    },

    UNDO{
        override fun serialize(): String {
            return "undo"
        }

    };

    abstract fun serialize(): String


    fun deserialize(s: String): Command {
        return when (s) {
            "play" -> PLAY
            "undo" -> UNDO
            else -> throw Error("bruh")
        }
    }
}