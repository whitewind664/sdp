package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Point
import io.socket.emitter.Emitter
import org.json.JSONObject

interface RealtimeService {

    fun awaitGame(gameID: String): ResponseListener<Boolean>

    fun connectToGame(playerID: String, gameID: String, onMove: (Point) -> Unit): ResponseListener<JSONObject>

    fun connect(accessToken: String)

    fun sendMove(move: Point, gameID: String)
}