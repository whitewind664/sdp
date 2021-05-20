package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import junit.framework.Assert.assertEquals
import org.junit.Test

class OGSChallengeTest {

    @Test
    fun toMapWorks() {
        val game = OGSGame("game")
        val challenge = OGSChallenge(game, Stone.WHITE)

        assertEquals(Stone.WHITE.toString(), challenge.toMap().get("color"))
    }

    @Test
    fun fromMapWorks() {

    }
}