package com.github.gogetters.letsgo.game.util.ogs

import android.util.Log
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.InputDelegate
import org.json.JSONObject
import java.lang.IllegalArgumentException

class OGSCommunicatorService(private val onlineService: OnlineService<JSONObject>,
                             private val CLIENT_ID: String,
                             private val CLIENT_SECRET: String) {
    //private val CLIENT_ID: String = "" // TODO
    //private val CLIENT_SECRET: String = "" // TODO
    private val base = "https://online-go.com"
    private val auth = "/api/v0/login"
    private val challenges = "/v1/challenges"
    private val games = "/v1/games"
    private var gameID = 0

    lateinit var inputDelegate: InputDelegate

    fun authenticate(username: String, password: String) {
        val body = JSONObject()
        body.put("client_id", CLIENT_ID)
        body.put("client_secret", CLIENT_SECRET)
        body.put("grant_type", "password")
        body.put("username", username)
        body.put("password", password)

        Log.i("JSONTEST", body.toString())

        onlineService.post("$base$auth", body, JSONObject()/*.put("Content-Type", "application/x-www-form-urlencoded")*/)
            .setOnResponse { onAuthenticationAccepted(it) }
    }


    fun startChallenge(challenge: OGSChallenge) {
        val body = challenge.toJSON()
        onlineService.post("$base$challenges", body).setOnResponse {
            val game = OGSGame.fromJSON(body.getJSONObject("game"))
        }
    }

    fun sendMove(move: Move) {
        val url = "$base$games/$gameID/move/"
        val body = JSONObject()
        val gtpMove = move.point.toString()
        val theirMove = gtpMove[0] + (gtpMove[1].toInt() + 'a'.toInt()).toChar().toString()
        body.put("move", theirMove)
        onlineService.post(url, body).setOnResponse {
            // TODO parse Move
        }

    }

    fun cancelChallenge(challengeID: String) {
        val url = "$base$challenges/$challengeID"

        onlineService.delete(url).setOnResponse {
            // TODO
        }
    }

    fun onAuthenticationAccepted(res: JSONObject) {
        Log.i("OGS_COMM", res.toString(4))
        //TODO("Not yet implemented")
    }

    fun onReceiveMove(move: Move) {
        TODO("Not yet implemented")
    }


    fun onChallengeAccepted(challengeData: OGSChallenge) {
        TODO("Not yet implemented")
    }

    companion object {
        fun parseMove(move: String): Point {
            if (move.length != 2) {
                throw IllegalArgumentException("could not parse move, must be 2 characters long")
            }

            val first = move[0].toInt() - 'a'.toInt() + 1
            val second = move[1].toInt() - 'a'.toInt() + 1

            if (first <= 0 || second <= 0) {
                throw IllegalArgumentException("invalid characters used, must be lowercase")
            }

            return Point(first, second)
        }
    }
}