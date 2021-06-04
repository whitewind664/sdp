package com.github.gogetters.letsgo.database.types

data class GameData(val player1: String = "", val player2: String = "", val ranked: Boolean = false) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "player1" to player1,
            "player2" to player2,
            "ranked" to ranked
        )
    }

    fun otherPlayer(myUid: String): String {
        if (player1 == myUid) {
            return player2
        } else {
            return player1
        }
    }
}