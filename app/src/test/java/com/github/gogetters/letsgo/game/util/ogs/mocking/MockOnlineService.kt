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

    override fun post(url: String, body: JSONObject): ResponseListener<JSONObject> {
        when (url) {

        }
    }

    override fun get(url: String): ResponseListener<JSONObject> {
        TODO("Not yet implemented")
    }

    override fun delete(url: String): ResponseListener<JSONObject> {
        TODO("Not yet implemented")
    }
}