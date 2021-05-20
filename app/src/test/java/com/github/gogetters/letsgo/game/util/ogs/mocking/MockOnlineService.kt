package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.ogs.*
import org.json.JSONObject
import java.lang.IllegalArgumentException
import kotlin.random.Random

class MockOnlineService : OnlineService<JSONObject> {
    var hasAuthenticated = false
    lateinit var username: String
    lateinit var password: String
    val challengeList = mutableListOf<OGSChallenge>()
    var currentGames = mutableListOf(OGSGame("first"), OGSGame("second"))
    var madeMove = false
    lateinit var lastMove: Point


    private val base = "https://online-go.com"
    private val auth = "/api/v0/login"
    private val myChallenges = "/v1/me/challenges/"
    private val challenges = "/v1/challenges"
    private val myGames = "/v1/me/games/"
    private val games = "/v1/games"


    override fun post(url: String, body: JSONObject, headers: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        when {
            url.startsWith("$base$auth") -> {
                hasAuthenticated = true
                username = body.getString("username")
                password = body.getString("password")
            }
            url.startsWith("$base$challenges") -> {
                val challenge = OGSChallenge.fromJSON(body)
                val game = OGSGame.fromJSON(body.getJSONObject("game"))
                challengeList.add(challenge)
                currentGames.add(game)

                val response = JSONObject()
                response.put("status", "ok")
                response.put("challenge", Random.nextInt(0, 65535))
                response.put("challenge", Random.nextInt(0, 65535))
                listener.onResponse(response)
            }
            url.startsWith("$base$games") -> {
                val response = JSONObject()
                val move = OGSCommunicatorService.parseMove(body.getString("move"))
                lastMove = move
                response.put("success", "Move Accepted")
                listener.onResponse(response)
            }
            else -> throw IllegalArgumentException("invalid API request, url does not match: $url")
        }

        return listener
    }

    override fun get(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        when {
            else -> throw IllegalArgumentException("should never send a get request??")
        }
    }

    override fun delete(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        val response = JSONObject()
        response.put("success", "Challenge removed")
        listener.onResponse(response)
        return listener
    }
}