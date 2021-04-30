package com.github.gogetters.letsgo.database.types

data class GameData(val player1: String = "", val player2: String = "") {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "player1" to player1,
            "player2" to player2
        )
    }
}