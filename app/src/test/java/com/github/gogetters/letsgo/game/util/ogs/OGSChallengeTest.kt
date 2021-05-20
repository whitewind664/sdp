package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.ogs.OGSChallenge.Companion.fromMap
import junit.framework.Assert.assertEquals
import okhttp3.Challenge
import org.junit.Test

class OGSChallengeTest {

    @Test
    fun toMapWorks() {
        val game = OGSGame("game")
        val challenge = OGSChallenge(game, Stone.WHITE)

        assertEquals(Stone.WHITE.toString(), challenge.toMap().get("challenger_color"))
    }

    @Test
    fun fromMapWorks() {
        val map: MutableMap<String,String> = mutableMapOf()
        map["challenger_color"] = Stone.WHITE.toString()

        assertEquals(fromMap(map).challengerColor, Stone.WHITE)
    }
}