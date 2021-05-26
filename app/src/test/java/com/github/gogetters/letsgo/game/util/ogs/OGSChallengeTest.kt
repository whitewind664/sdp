package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.Test

class OGSChallengeTest {

    @Test
    fun toJsonWorks() {
        val game = OGSGame("game")
        val min = 5
        val challenge = OGSChallenge(game, Stone.WHITE, minRanking = min)
        val json = challenge.toJSON()

        assertEquals(Stone.WHITE.toString(), json["challenger_color"])
        assertEquals(min, json["min_ranking"])
    }

    @Test
    fun fromJsonWorks() {
        var json = JSONObject()
        json.put("challenger_color", Stone.WHITE.toString())
        json.put("game", OGSGame("mygame").toJSON())

        assertEquals(OGSChallenge.fromJSON(json).challengerColor, Stone.WHITE)

        json = JSONObject()
        json.put("challenger_color", Stone.BLACK.toString())
        json.put("game", OGSGame("mygame").toJSON())

        assertEquals(OGSChallenge.fromJSON(json).challengerColor, Stone.BLACK)
    }
}