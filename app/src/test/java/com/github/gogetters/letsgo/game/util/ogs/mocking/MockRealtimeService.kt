package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.ogs.RealtimeService

class MockRealtimeService: RealtimeService {
    override fun connectToGame(playerID: String, gameID: String, onMove: (Point) -> Unit) {

    }

    override fun connect(accessToken: String) {

    }

    override fun sendMove(move: Point, gameID: String) {

    }

}