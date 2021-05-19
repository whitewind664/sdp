package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Move

interface OGSCommunicator {

    fun authenticate(username: String, password: String)
    fun onAuthenticationAccepted()

    fun startChallenge(ogsGame: OGSGame)
    fun onChallengeAccepted(challengeData: String) //Maybe we change to a custom representation, for now JSON
    fun sendMove(move: Move)
    fun onReceiveMove(move: Move)
    fun listActiveGames(): String
    fun cancelChallenge(challengeID: String) // maybe we just store the active challenge so we don't pass it

}