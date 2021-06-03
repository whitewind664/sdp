package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.ogs.RealtimeService
import com.github.gogetters.letsgo.game.util.ogs.ResponseListener
import org.json.JSONObject

class MockRealtimeService: RealtimeService {
    override fun awaitGame(gameID: String): ResponseListener<Boolean> {
        return ResponseListener()
    }

    override fun connectToGame(playerID: String, gameID: String, onMove: (Point) -> Unit): ResponseListener<JSONObject> {
        return ResponseListener()
    }

    override fun connect(accessToken: String) {

    }

    override fun sendMove(move: Point, gameID: String) {

    }

}