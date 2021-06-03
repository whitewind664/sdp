package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.ogs.mocking.MockOnlineService
import com.github.gogetters.letsgo.game.util.ogs.mocking.MockRealtimeService
import junit.framework.Assert.*
import org.junit.Test

class OGSCommunicatorTest {

    @Test
    fun authenticationSendsUsernameAndPassword() {
        val service = MockOnlineService()
        val communicator = OGSCommunicatorService(service, MockRealtimeService(), "", "")
        val username = "john"
        val password = "doe"

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
        val communicator = OGSCommunicatorService(service, MockRealtimeService(),"", "")

        val existingGames = service.currentGames.size

        communicator.authenticate("john", "doe")
        communicator.startChallenge()
        assertEquals(1, service.challengeList.size)
        assertEquals(existingGames + 1, service.currentGames.size)
    }

    @Test
    fun sendMoveUpdatesGame() {
        val service = MockOnlineService()
        val communicator = OGSCommunicatorService(service, MockRealtimeService(),"", "")

        val existingGames = service.currentGames.size

        val move = Point(4,5)

        communicator.authenticate("john", "doe")
        communicator.startChallenge()
        assertEquals(1, service.challengeList.size)
        assertEquals(existingGames + 1, service.currentGames.size)
        communicator.sendMove(move, "0")
        // assertEquals(move, service.lastMove)
        // TODO encoding of move is not yet correct
    }
}