package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import org.json.JSONObject
import java.lang.IllegalArgumentException

data class OGSChallenge(
    val game: OGSGame,
    val challengerColor: Stone,
    val minRanking: Int = 0,
    val maxRanking: Int = 0) {

    fun toMap(): MutableMap<String, String> {
        //val game = game.toJSON()
        val map: MutableMap<String, String> = mutableMapOf<String, String>()
        //map.put("game", game)
        map["challenger_color"] = challengerColor.toString()
        map["min_ranking"] = minRanking.toString()
        map["max_ranking"] = maxRanking.toString()
        return map
    }


    override fun toString(): String {
        return toMap().toString()
    }

    companion object {

        fun fromMap(challenge: MutableMap<String, String>): OGSChallenge {

            val color = when (challenge.get("challenger_color")) {
                "white" -> Stone.WHITE
                "black" -> Stone.BLACK
                "automatic" -> Stone.BLACK //todo :(((((((
                else -> throw IllegalArgumentException("could not parse stone color ${challenge.get("challenger_color")}")
            }

            //val game = OGSGame.fromJSON(challenge.get("game"))
            val game = OGSGame("name")
            /**return OGSChallenge(game, color,
                    challenge.get("min_ranking"),
                    challenge.get("max_ranking"))*/
            return OGSChallenge(game, color)
        }
    }
}
