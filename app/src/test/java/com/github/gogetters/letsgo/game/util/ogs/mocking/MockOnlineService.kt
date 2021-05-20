package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.ogs.OGSChallenge
import com.github.gogetters.letsgo.game.util.ogs.OGSGame
import com.github.gogetters.letsgo.game.util.ogs.OnlineService
import com.github.gogetters.letsgo.game.util.ogs.ResponseListener
import org.json.JSONArray
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.util.*

class MockOnlineService : OnlineService<JSONObject> {
    var hasAuthenticated = false
    var currentGames = listOf(OGSGame("first"), OGSGame("second"))
    var madeMove = false
    var challengeList = listOf<OGSChallenge>()
    private val base = "http://online-go.com"
    private val auth = "/oauth2/access_token"
    private val myChallenges = "/v1/me/challenges/"
    private val challenges = "/v1/challenges"
    private val myGames = "/v1/me/games/"
    private val games = "/v1/games"


    override fun post(url: String, body: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        when {
            url.startsWith("$base$auth") -> hasAuthenticated = true
            url.startsWith("$base$myChallenges") ->  {
                val challengesJSON = JSONArray(challengeList)
                listener.onResponse(challengesJSON)
            }
        }
    }

    override fun get(url: String): ResponseListener<JSONObject> {
        TODO("Not yet implemented")
    }

    override fun delete(url: String): ResponseListener<JSONObject> {
        TODO("Not yet implemented")
    }
}