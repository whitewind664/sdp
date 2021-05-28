package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.InputDelegate
import org.json.JSONException
import org.json.JSONObject
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class OGSCommunicatorService(private val onlineService: OnlineService<JSONObject>,
                             private val CLIENT_ID: String,
                             private val CLIENT_SECRET: String) {


    private val base = "https://online-go.com"
    private val auth = "/oauth2/token/"
    //private val challenges = "/v1/challenges" //TODO
    private val challenges = "api/v1/players/800307/challenge/"
    private val games = "/v1/games"
    private var gameID = 0
    lateinit var accessToken: String
    lateinit var refreshToken: String

    //TODO separate into mockable realtime
    private val socket: Socket = IO.socket("")

    lateinit var activeChallenge: OGSChallenge
    var authenticated = false

    lateinit var inputDelegate: InputDelegate

    private val onMove = Emitter.Listener {
        val data = it[0] as JSONObject
        val move = data.getString("move")
        inputDelegate.saveLatestInput(Point.fromSGF(move))
    }



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

        socket.connect()

        return after
    }


    fun startChallenge(challenge: OGSChallenge) {
        val body = challenge.toJSON()
        onlineService.post("$base$challenges", body).setOnResponse {
            val gameID = it.getString("game")
            val challengeID = it.getString("challenge")

            activeChallenge = OGSChallenge(challengeID, OGSGame("bot_challenge", gameID),
            Stone.WHITE, -1000, 1000)
            //TODO isolate socket
            socket.emit("game/connect", JSONObject())
            socket.on("game/$gameID/move", onMove)
        }
    }

    fun cancelChallenge(challengeID: String) {
        val url = "$base$challenges/$challengeID"

        onlineService.delete(url).setOnResponse {
            // TODO
        }
    }

    fun onChallengeAccepted(challengeData: OGSChallenge) {
        TODO("Not yet implemented")
    }

    fun sendMove(move: Move, gameID: String) {
        val point = move.point.toSGF()
        val body = JSONObject()
        //TODO finish
        body.put("auth", accessToken)
        body.put("game_id", gameID)
        body.put("player_id", "ourid...")
        body.put("move", point)

        socket.emit("game/move", body)
    }
}