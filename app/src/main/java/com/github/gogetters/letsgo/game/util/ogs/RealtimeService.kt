package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Point
import io.socket.emitter.Emitter

interface RealtimeService {

    fun awaitGame(gameID: String): ResponseListener<Boolean>

    fun connectToGame(playerID: String, gameID: String, onMove: (Point) -> Unit)

    fun connect(accessToken: String)

    fun sendMove(move: Point, gameID: String)
}