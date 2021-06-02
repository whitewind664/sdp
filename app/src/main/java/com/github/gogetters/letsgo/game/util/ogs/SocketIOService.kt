package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Point
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

class SocketIOService : RealtimeService {

    private val socket: Socket = IO.socket("https://online-go.com/socket.io/?EIO=3")

    private lateinit var accessToken: String

    override fun connectToGame(playerID: String, gameID: String, onMove: (Point) -> Unit) {
        val gameDetails = JSONObject()
        gameDetails.put("player_id", "our_id...")
        gameDetails.put("game_id", gameID)
        gameDetails.put("chat", false)


        val emitter = Emitter.Listener {
            val data = it[0] as JSONObject
            val move = data.getString("move")
            onMove(Point.fromSGF(move))
        }

        socket.emit("game/connect", gameDetails)
        socket.on("game/$gameID/move", emitter)
    }

    override fun connect(accessToken: String) {
        this.accessToken = accessToken
        socket.connect()
    }

    override fun sendMove(move: Point, gameID: String) {
        val point = move.toSGF()
        val body = JSONObject()

        body.put("auth", accessToken)
        body.put("game_id", gameID)
        body.put("player_id", "ourid...")
        body.put("move", point)

        socket.emit("game/move", body)
    }
}