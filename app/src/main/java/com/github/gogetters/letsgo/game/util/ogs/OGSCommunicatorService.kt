package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.util.InputDelegate
import org.json.JSONObject

class OGSCommunicatorService(private val onlineService: OnlineService) : OGSCommunicator {
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

        //TODO maybe not the best... requests are async, what if we do 2 at once?
        //TODO maybe best would be some sort of sendRequest().onresponse(x -> y) but how?
        onlineService.responseListener =
            OnlineService.ResponseListener { onAuthenticationAccepted() }
        onlineService.post("$base/oauth2/access_token", body.toString())
    }

    override fun onAuthenticationAccepted() {
        TODO("Not yet implemented")
    }

    override fun startChallenge(challenge: OGSChallenge) {
        val body = challenge.toJSON()
        onlineService.responseListener = OnlineService.ResponseListener { }
        onlineService.post("$base/v1/me/challenges/", body.toString(4))
    }

    override fun onChallengeAccepted(challengeData: String) {
        TODO("Not yet implemented")
    }

    override fun sendMove(move: Move) {
        val url = "$base/v1/games/$gameID/move/"
        val body = JSONObject()
        body.put("move", move.point.toString())
        onlineService.responseListener = OnlineService.ResponseListener {  }
        onlineService.post(url, body.toString(4))

    }

    override fun onReceiveMove(move: Move) {
        TODO("Not yet implemented")
    }

    override fun listActiveGames(): String {
        val url = "$base/v1/me/games/"
        onlineService.responseListener = OnlineService.ResponseListener {  }
        onlineService.get(url)
        return "" //TODO.... not sure
    }

    override fun cancelChallenge(challengeID: String) {
        val url = "$base/v1/challenges/$challengeID"

        onlineService.responseListener = OnlineService.ResponseListener {  }
        onlineService.delete(url)
    }
}