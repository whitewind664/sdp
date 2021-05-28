package com.github.gogetters.letsgo.game.util.ogs

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class OGSGameTest {

    @Test
    fun gameSerializesCorrectly() {
        val game = OGSGame("0", "mygame")
        val json = game.toJSON()

        assertEquals(game.name, json.getString("name"))
        assertEquals(game.height, json.getInt("height"))
        assertEquals(game.width, json.getInt("width"))
    }

    @Test
    fun encodingAndDecodingResultsInSameGame() {
        val game = OGSGame("0", "agame")
        assertEquals(game, OGSGame.fromJSON(game.toJSON()))
    }
}