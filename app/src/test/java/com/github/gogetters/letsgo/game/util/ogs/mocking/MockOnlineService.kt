package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.ogs.OGSChallenge
import com.github.gogetters.letsgo.game.util.ogs.OGSGame
import com.github.gogetters.letsgo.game.util.ogs.OnlineService
import com.github.gogetters.letsgo.game.util.ogs.ResponseListener
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.util.*

class MockOnlineService : OnlineService<JSONObject> {
    var hasAuthenticated = false
    var currentGames = listOf(OGSGame("first"), OGSGame("second"))
    var madeMove = false
    var challenges = listOf<OGSChallenge>()

    fun challengeFromJSON(challenge: JSONObject): OGSChallenge {

        val color = when (challenge.getString("challenger_color")) {
            "white" -> Stone.WHITE
            "black" -> Stone.BLACK
            "automatic" -> Stone.BLACK //todo :(((((((
            else -> throw IllegalArgumentException("could not parse stone color")
        }

        val game = gameFromJSON(challenge.getJSONObject("game"))
        return OGSChallenge(game, color,
            challenge.getInt("min_ranking"),
            challenge.getInt("max_ranking"))
    }

    private fun gameFromJSON(game: JSONObject): OGSGame {
        return OGSGame(game.getString("name"), game.getInt("width"), game.getInt("height"))
    }

    override fun post(url: String, body: JSONObject): ResponseListener<JSONObject> {
        TODO("Not yet implemented")
    }

    override fun get(url: String): ResponseListener<JSONObject> {
        TODO("Not yet implemented")
    }

    override fun delete(url: String): ResponseListener<JSONObject> {
        TODO("Not yet implemented")
    }
}