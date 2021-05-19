package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Move
import org.json.JSONObject

class OGSCommunicatorService(private val onlineService: OnlineService) : OGSCommunicator {
    private val CLIENT_ID: String = "" // TODO
    private val CLIENT_SECRET: String = "" // TODO

    override fun authenticate(username: String, password: String) {
        var body = JSONObject()
        body.put("client_id", CLIENT_ID)
        body.put("client_secret", CLIENT_SECRET)
        body.put("username", username)
        body.put("password", password)

        onlineService.sendRequest("http://online-go.com/oauth2/access_token", body.toString())
    }

    override fun onAuthenticationAccepted() {
        TODO("Not yet implemented")
    }

    override fun startChallenge(ogsGame: OGSGame) {
        TODO("Not yet implemented")
    }

    override fun onChallengeAccepted(challengeData: String) {
        TODO("Not yet implemented")
    }

    override fun sendMove(move: Move) {
        TODO("Not yet implemented")
    }

    override fun onReceiveMove(move: Move) {
        TODO("Not yet implemented")
    }

    override fun listActiveGames(): String {
        TODO("Not yet implemented")
    }

    override fun cancelChallenge(challengeID: String) {
        TODO("Not yet implemented")
    }
}