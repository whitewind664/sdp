package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.ogs.*
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.IllegalArgumentException
import kotlin.random.Random

class MockOnlineService : OnlineService<String> {
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


    override fun post(url: String, body: Map<String, String>, headers: Map<String, String>): ResponseListener<String> {
        val listener = ResponseListener<String>()
        when {
            url.startsWith("$base$auth") -> {
                hasAuthenticated = true
                username = body["username"]!!
                password = body["password"]!!
            }
            url.startsWith("$base$challenges") -> {
                //TODO use GSON to convert to map
                val challenge = OGSChallenge.fromMap(body.toMutableMap())
                challengeList.add(challenge)
                currentGames.add(challenge.game)
            }
            url.startsWith("$base$games") -> {
                val response = mutableMapOf<String, String>()
                val move = OGSCommunicatorService.parseMove(body["move"]!!)
                lastMove = move
                response["success"] = "Move Accepted"
                listener.onResponse(Gson().toJson(response))
            }
            else -> throw IllegalArgumentException("invalid API request, url does not match: $url")
        }

        return listener
    }

    override fun get(url: String, headers: Map<String, String>): ResponseListener<String> {
        val listener = ResponseListener<String>()
        when {
            else -> throw IllegalArgumentException("should never send a get request??")
        }
    }

    override fun delete(url: String, headers: Map<String, String>): ResponseListener<String> {
        val listener = ResponseListener<String>()
        val response = mutableMapOf<String, String>()
        response["success"] = "Challenge removed"
        listener.onResponse(Gson().toJson(response))
        return listener
    }
}