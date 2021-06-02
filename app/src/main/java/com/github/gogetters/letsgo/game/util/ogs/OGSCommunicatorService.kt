package com.github.gogetters.letsgo.game.util.ogs

import android.util.Log
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.InputDelegate
import org.json.JSONException
import org.json.JSONObject
import java.net.CookieManager
import java.net.URI

class OGSCommunicatorService(private val onlineService: OnlineService<JSONObject>,
                             private val realtimeService: RealtimeService,
                             private val CLIENT_ID: String,
                             private val CLIENT_SECRET: String) {


    private val base = "https://online-go.com"
    private val auth = "/oauth2/token/"
    private val login = "/api/v0/login"
    //private val challenges = "/v1/challenges" //TODO
    private val challenges = "/api/v1/challenges"
    private val botChallenges = "/api/v1/players/800307/challenge/"
    private val games = "/v1/games"
    private var gameID = 0
    lateinit var accessToken: String
    lateinit var refreshToken: String

    lateinit var activeChallenge: OGSChallenge
    var authenticated = false

    lateinit var inputDelegate: InputDelegate

    private fun login(username: String, password: String): ResponseListener<Boolean> {
        val body = JSONObject()
        body.put("client_id", CLIENT_ID)
        body.put("client_secret", CLIENT_SECRET)
        body.put("grant_type", "password")
        body.put("username", username)
        body.put("password", password)

        val response =  onlineService.post("$base$login", body)
        val after = ResponseListener<Boolean>()
        response.setOnResponse {
            try {
                val testUsername = it.getJSONObject("user").getString("username")
                after.onResponse(testUsername == username)
                Log.d("TEST TEST", testUsername)
            } catch (e: JSONException) {
                after.onResponse(false)
                Log.d("LOGIN FAILED", it.toString())
            }
        }

        return after
    }


    fun authenticate(username: String, password: String): ResponseListener<Boolean> {
        val usernameTest = "kimonroxd"
        val passwordTest = "online-go.com"
        val after = ResponseListener<Boolean>()
        login(usernameTest, passwordTest).setOnResponse { loginSuccess ->
            if (loginSuccess)  {
                val body = JSONObject()
                body.put("client_id", CLIENT_ID)
                body.put("client_secret", CLIENT_SECRET)
                body.put("grant_type", "password")
                body.put("username", usernameTest)
                body.put("password", passwordTest)

                val response =  onlineService.post("$base$auth", body)
                response.setOnResponse {
                    try {
                        accessToken = it.getString("access_token")
                        refreshToken = it.getString("refresh_token")
                        after.onResponse(true)
                        authenticated = true

                    } catch (e: JSONException) {
                        after.onResponse(false)
                    }
                }
            } else {
                after.onResponse(false)
            }
        }

        return after
    }


    fun startChallenge(challenge: OGSChallenge) {
        if (!authenticated) throw IllegalStateException("user has not authenticated yet")

        val body = challenge.toJSON()
        //TODO change to real challenges
        val cookies = CookieManager.getDefault().get(URI(base), mapOf())
        val cookieString = cookies["Cookie"]!![0]
        val token = cookieString.subSequence(
                cookieString.indexOf("csrftoken=") + "csrftoken=".length,
                cookieString.length).toString()

        val headers = JSONObject()
        headers.put("x-csrftoken", token)
        headers.put("referer", base)
        headers.put("content-type", "application/json")
        val temp = "http://127.0.0.1"
        onlineService.post("$base$botChallenges", body, headers).setOnResponse { response ->
            val gameID = response.getString("game")
            val challengeID = response.getString("challenge")

            activeChallenge = OGSChallenge(challengeID, OGSGame("bot_challenge", gameID),
                    Stone.BLACK, -1000, 1000)

            realtimeService.connectToGame("ourid...", gameID) { inputDelegate.saveLatestInput(it) }
        }
    }

    fun cancelChallenge(challengeID: String) {
        val url = "$base$challenges/$challengeID"

        onlineService.delete(url).setOnResponse {
            // TODO
        }
    }

    fun sendMove(move: Point, gameID: String) {
        realtimeService.sendMove(move, gameID)
    }
}