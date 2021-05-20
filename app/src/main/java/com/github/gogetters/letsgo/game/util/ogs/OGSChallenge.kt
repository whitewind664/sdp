package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import org.json.JSONObject

data class OGSChallenge(
    val game: OGSGame,
    val challengerColor: Stone,
    val minRanking: Int = 0,
    val maxRanking: Int = 0) {

    fun toJSON(): JSONObject {
        val game = game.toJSON()
        val challenge = JSONObject()
        challenge.put("game", game)
        challenge.put("challenger_color", challengerColor)
        challenge.put("min_ranking", minRanking)
        challenge.put("max_ranking", maxRanking)
        return challenge
    }

    override fun toString(): String {
        return toJSON().toString(4)
    }
}
