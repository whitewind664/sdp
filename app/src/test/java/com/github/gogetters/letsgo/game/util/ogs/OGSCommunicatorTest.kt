package com.github.gogetters.letsgo.game.util.ogs

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class OGSCommunicatorTest {

    @Test
    fun newChallengeAfterCreateChallenge() {
        val service = TestOnlineService()
        val communicator = OGSCommunicatorService(service)

        assertFalse(service.hasAuthenticated)
        //TODO would be nice to set here "onAuthenticationAccepted" for example
        communicator.authenticate("user", "password")
        assertTrue(service.hasAuthenticated)


    }
}