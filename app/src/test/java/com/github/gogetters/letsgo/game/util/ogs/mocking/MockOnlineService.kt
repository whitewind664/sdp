package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.ogs.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.IllegalArgumentException
import kotlin.random.Random

class MockOnlineService : OnlineService<JSONObject> {
    var hasAuthenticated = false
    lateinit var id: String
    lateinit var secret: String
    var currentGames = listOf(OGSGame("first"), OGSGame("second"))
    var madeMove = false
    lateinit var lastMove: Point
    var challengeList = listOf<OGSChallenge>()
    private val base = "http://online-go.com"
    private val auth = "/oauth2/access_token"
    private val myChallenges = "/v1/me/challenges/"
    private val challenges = "/v1/challenges"
    private val myGames = "/v1/me/games/"
    private val games = "/v1/games"


    override fun post(url: String, body: JSONObject, headers: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        when {
            url.startsWith("$base$auth") -> {
                hasAuthenticated = true
                id = body.getString("client_id")
                secret = body.getString("client_secret")
            }
            url.startsWith("$base$challenges") -> {
                val response = JSONObject()
                response.put("status", "ok")
                response.put("challenge", Random.nextInt(0, 65535))

            }
            url.startsWith("$base$myGames") -> {
                val gamesJSON = JSONObject().put("games", JSONArray(currentGames))
                listener.onResponse(gamesJSON)
            }
            url.startsWith("$base$games") -> {
                val response = JSONObject()
                val move = OGSCommunicatorService.parseMove(body.getString("move"))
                lastMove = move
                response.put("success", "Move Accepted")
                listener.onResponse(response)
            }
            else -> throw IllegalArgumentException("invalid API request")
        }

        return listener
    }

    override fun get(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        when {
            url.startsWith("$base$myChallenges") ->  {
                val challengesJSON = JSONObject().put("challenges", JSONArray(challengeList))
                listener.onResponse(challengesJSON)
            }
        }
    }

    override fun delete(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        TODO("Not yet implemented")
    }
}