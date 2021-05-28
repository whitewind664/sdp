package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import org.json.JSONObject
import java.lang.IllegalArgumentException

data class OGSChallenge(
    val id: String,
    val game: OGSGame,
    val challengerColor: Stone,
    val minRanking: Int = 0,
    val maxRanking: Int = 0) {

    //TODO change string literals to "const"

    fun toJSON(): JSONObject {
        val game = game.toJSON()
        val challenge = JSONObject()
        challenge.put("game", game)
        challenge.put("challenger_color", challengerColor.toString())
        challenge.put("min_ranking", minRanking)
        challenge.put("max_ranking", maxRanking)
        return challenge
    }


    override fun toString(): String {
        return toJSON().toString(4)
    }

    companion object {

        fun fromJSON(challenge: JSONObject): OGSChallenge {
            val color = when (challenge.getString("challenger_color")) {
                "white" -> Stone.WHITE
                "black" -> Stone.BLACK
                "automatic" -> Stone.BLACK //todo :(((((((
                else -> throw IllegalArgumentException("could not parse stone color ${challenge.getString("challenger_color")}")
            }

            val min = if (challenge.has("min_ranking")) challenge.getInt("min_ranking") else 0
            val max = if (challenge.has("max_ranking")) challenge.getInt("max_ranking") else 0

            val game = OGSGame.fromJSON(challenge.getJSONObject("game"))

            val id = challenge.getString("id")

            return OGSChallenge(id, game, color, min, max)
        }
    }
}
