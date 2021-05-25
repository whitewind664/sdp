package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.Test

class OGSChallengeTest {

    @Test
    fun toJsonWorks() {
        val game = OGSGame("game")
        val challenge = OGSChallenge(game, Stone.WHITE)

        assertEquals(Stone.WHITE.toString(), challenge.toJSON()["challenger_color"])
    }

    @Test
    fun fromMapWorks() {
        val json = JSONObject()
        json.put("challenger_color", Stone.WHITE.toString())

        assertEquals(OGSChallenge.fromJSON(json).challengerColor, Stone.WHITE)
    }
}