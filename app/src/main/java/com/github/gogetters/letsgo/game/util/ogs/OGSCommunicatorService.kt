package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Move

class OGSCommunicatorService(private val onlineService: OnlineService) : OGSCommunicator {

    override fun authenticate(username: String, password: String) {
        TODO("Not yet implemented")
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