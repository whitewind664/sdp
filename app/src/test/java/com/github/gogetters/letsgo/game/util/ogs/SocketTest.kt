package com.github.gogetters.letsgo.game.util.ogs

import android.util.Log
import io.socket.client.IO
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.net.URI

class SocketTest {

    @Test
    fun connectToGameWorks() {

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

    @Test
    fun connectWorks() {
        val url = "https://online-go.com/socket.io/?EIO=3"
        val socket = IO.socket(URI(url))
        socket.connect()

        Thread.sleep(10000)
        assertTrue(socket.connected())
    }

}