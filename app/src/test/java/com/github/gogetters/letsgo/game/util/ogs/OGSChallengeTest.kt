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
    fun encodingAndDecodingReturnsSameChallenge() {
        val game = OGSGame("game")
        val challenge = OGSChallenge(game, Stone.WHITE)

        assertEquals(challenge, OGSChallenge.fromJSON(challenge.toJSON()))
    }
}