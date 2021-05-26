package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.lang.IllegalArgumentException


class OGSChallengeTest {

    @get:Rule
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun toJsonWorks() {
        val game = OGSGame("game")
        val challenge = OGSChallenge(game, Stone.WHITE, 999, 382)

        val json = challenge.toJSON()

        assertEquals(challenge.challengerColor.toString(), json.getString("challenger_color"))
        assertEquals(challenge.game, OGSGame.fromJSON(json.getJSONObject("game")))
        assertEquals(challenge.minRanking, json.getInt("min_ranking"))
        assertEquals(challenge.maxRanking, json.getInt("max_ranking"))
    }

    @Test
    fun encodingAndDecodingReturnsSameChallenge() {
        val game = OGSGame("game")
        val challenge = OGSChallenge(game, Stone.WHITE)

        assertEquals(challenge, OGSChallenge.fromJSON(challenge.toJSON()))
    }

    @Test
    fun fromJSONThrowsOnInvalidStoneValue() {
        exception.expect(IllegalArgumentException::class.java)

        val game = OGSGame("name")
        val json = JSONObject()
        json.put("game", game)
        json.put("challenger_color", "invalidColor")
        json.put("min_ranking", 0)
        json.put("max_ranking", 0)
        OGSChallenge.fromJSON(json)
    }
}