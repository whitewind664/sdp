package com.github.gogetters.letsgo.game.util.ogs

import android.util.Log
import junit.framework.Assert.assertTrue
import org.junit.Test

class SocketTest {

    @Test
    fun connectWorks() {

        val realtime = SocketIOService()

        var worked = false
        val gameID = "34226369"
        //realtime.connect("")
        Log.d("CONNECTING TO GAME", "CONNECTING...")
        realtime.connectToGame("890473", gameID) {
            Log.d("RECEIVED MOVE", "$it")
        }.setOnResponse {
            Log.d("RECEIVED GAME DATA", "$it")
            worked = true
        }
        Thread.sleep(10000)
        assertTrue(worked)
    }

}