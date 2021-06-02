package com.github.gogetters.letsgo.game.util.ogs

import com.android.volley.Response
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.InputDelegate
import org.json.JSONException
import org.json.JSONObject

class OGSCommunicatorService(private val onlineService: OnlineService<JSONObject>,
                             private val realtimeService: RealtimeService,
                             private val CLIENT_ID: String,
                             private val CLIENT_SECRET: String) {


    private val base = "https://online-go.com"
    private val auth = "/oauth2/token/"
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


    fun authenticate(username: String, password: String): ResponseListener<Boolean> {
        val body = JSONObject()
        body.put("client_id", CLIENT_ID)
        body.put("client_secret", CLIENT_SECRET)
        body.put("grant_type", "password")
        body.put("username", username)
        body.put("password", password)

        val response =  onlineService.post("$base$auth", body)
        val after = ResponseListener<Boolean>()
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


        return after
    }


    fun startChallenge(challenge: OGSChallenge) {
        val body = challenge.toJSON()
        //TODO change to real challenges
        onlineService.post("$base$botChallenges", body).setOnResponse { response ->
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