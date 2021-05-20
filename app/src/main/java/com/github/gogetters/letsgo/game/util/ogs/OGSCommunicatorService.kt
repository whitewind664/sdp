package com.github.gogetters.letsgo.game.util.ogs

import android.util.Log
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.IllegalArgumentException

class OGSCommunicatorService(private val onlineService: OnlineService<String>,
                             private val CLIENT_ID: String,
                             private val CLIENT_SECRET: String) {
    //private val CLIENT_ID: String = "" // TODO
    //private val CLIENT_SECRET: String = "" // TODO
    private val base = "https://online-go.com"
    private val auth = "/api/v0/login"
    private val myChallenges = "/v1/me/challenges/"
    private val challenges = "/v1/challenges"
    private val myGames = "/v1/me/games/"
    private val games = "/v1/games"
    private var gameID = 0

    lateinit var inputDelegate: InputDelegate

    fun authenticate(username: String, password: String) {
        val body = mutableMapOf<String, String>()
        body["client_id"] = CLIENT_ID
        body["client_secret"] = CLIENT_SECRET
        body["grant_type"] = "password"
        body["username"] = username
        body["password"] = password

        val header = mutableMapOf<String, String>()
        header.put("Content-Type", "application/x-www-form-urlencoded")

        onlineService.post("$base$auth", body, header)
            .setOnResponse { onAuthenticationAccepted(it) }
    }


    fun startChallenge(challenge: OGSChallenge) {
    }

    fun sendMove(move: Move) {
        val url = "$base$games/$gameID/move/"
        val gtpMove = move.point.toString()
        val theirMove = gtpMove[0] + (gtpMove[1].toInt() + 'a'.toInt()).toChar().toString()

        val body = mutableMapOf<String, String>()
        body["move"] = theirMove
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

    fun onAuthenticationAccepted(res: String) {
        Log.i("OGS_COMM", res)
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