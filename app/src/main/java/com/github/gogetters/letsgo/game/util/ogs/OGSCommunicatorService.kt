package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.util.InputDelegate
import org.json.JSONObject

class OGSCommunicatorService(private val onlineService: OnlineService<JSONObject>) : OGSCommunicator {
    private val CLIENT_ID: String = "" // TODO
    private val CLIENT_SECRET: String = "" // TODO
    private val base = "http://online-go.com"
    private var gameID = 0

    lateinit var inputDelegate: InputDelegate

    override fun authenticate(username: String, password: String) {
        val body = JSONObject()
        body.put("client_id", CLIENT_ID)
        body.put("client_secret", CLIENT_SECRET)
        body.put("username", username)
        body.put("password", password)

        onlineService.post("$base/oauth2/access_token", body)
            .setOnResponse { onAuthenticationAccepted() }
    }

    override fun onAuthenticationAccepted() {
        TODO("Not yet implemented")
    }

    override fun startChallenge(challenge: OGSChallenge) {
        val body = challenge.toJSON()
        onlineService.post("$base/v1/me/challenges/", body)
            .setOnResponse { onChallengeAccepted(OGSChallenge.fromJSON(it)) }
    }

    override fun onChallengeAccepted(challenge: OGSChallenge) {
        TODO("Not yet implemented")
    }

    override fun sendMove(move: Move) {
        val url = "$base/v1/games/$gameID/move/"
        val body = JSONObject()
        val gtpMove = move.point.toString()
        val theirMove = gtpMove[0] + (gtpMove[1].toInt() + 'a'.toInt()).toChar().toString()
        body.put("move", theirMove)
        onlineService.post(url, body).setOnResponse {
            // TODO parse Move
        }

    }

    override fun onReceiveMove(move: Move) {
        TODO("Not yet implemented")
    }

    override fun listActiveGames(): String {
        val url = "$base/v1/me/games/"
        onlineService.get(url).setOnResponse {
            // TODO
        }
        return "" //TODO.... not sure
    }

    override fun cancelChallenge(challengeID: String) {
        val url = "$base/v1/challenges/$challengeID"

        onlineService.delete(url).setOnResponse {
            // TODO
        }
    }
}