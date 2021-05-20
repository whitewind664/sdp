package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.ogs.mocking.MockOnlineService
import junit.framework.Assert.*
import org.json.JSONObject
import org.junit.Test

class OGSCommunicatorTest {

    @Test
    fun authenticationSendsUsernameAndPassword() {
        val service = MockOnlineService()
        val communicator = OGSCommunicatorService(service, "", "")
        val username = "john"
        val password = "burgur"

        assertFalse(service.hasAuthenticated)
        //TODO would be nice to set here "onAuthenticationAccepted" for example
        communicator.authenticate(username, password)
        assertTrue(service.hasAuthenticated)

        assertEquals(username, service.username)
        assertEquals(password, service.password)
    }

    @Test
    fun createChallengeSendsChallengeInfo() {
        val service = MockOnlineService()
        val communicator = OGSCommunicatorService(service, "", "")
        val challenge = OGSChallenge(OGSGame("mygame"), Stone.BLACK)

        val existingGames = service.currentGames.size

        communicator.startChallenge(challenge)
        assertEquals(1, service.challengeList.size)
        assertEquals(existingGames + 1, service.currentGames.size)
    }

    @Test
    fun sendMoveUpdatesGame() {
        val service = MockOnlineService()
        val communicator = OGSCommunicatorService(service, "", "")

        val game = OGSGame("mygame")
        game.toString()
        val challenge = OGSChallenge(game, Stone.BLACK)
        challenge.toString()

        val existingGames = service.currentGames.size

        val move = Move(Stone.BLACK, Point(4,5))

        communicator.startChallenge(challenge)
        assertEquals(1, service.challengeList.size)
        assertEquals(existingGames + 1, service.currentGames.size)
        communicator.sendMove(move)
        assertEquals(move.point, service.lastMove)
    }
}