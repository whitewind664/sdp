package com.github.gogetters.letsgo.game.util.ogs

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class OGSGameTest {

    @Test
    fun gameSerializesCorrectly() {
        val game = OGSGame("mygame")
        assertTrue(game.toJSON() != null)
        assertEquals("mygame", game.toJSON().getString("name"))
    }

    @Test
    fun encodingAndDecodingResultsInSameGame() {
        val game = OGSGame("agame")
        assertEquals(game, OGSGame.fromJSON(game.toJSON()))
    }
}