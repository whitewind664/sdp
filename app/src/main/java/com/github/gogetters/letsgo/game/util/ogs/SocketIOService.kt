package com.github.gogetters.letsgo.game.util.ogs

import android.util.Log
import com.github.gogetters.letsgo.game.Point
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

//TODO can easily add more tests by isolating the calls to "socket" under another interface
class SocketIOService : RealtimeService {

    private val socket: Socket = IO.socket("https://online-go.com/socket.io/?EIO=3")

    private lateinit var accessToken: String

    override fun awaitGame(gameID: String): ResponseListener<Boolean> {

        val responseListener = ResponseListener<Boolean>()
        val game = JSONObject()
        game.put("game_id", gameID)
        socket.emit("game/wait", game)
        socket.on("game/$gameID/reset") {
            Log.d("SOCKET SOCKET", "we got handshake from game")
            responseListener.onResponse(true)
        }
        //TODO remove
        responseListener.onResponse(true)
        return responseListener
    }

    override fun connectToGame(playerID: String, gameID: String, onMove: (Point) -> Unit): ResponseListener<JSONObject> {
        val gameDetails = JSONObject()
        gameDetails.put("player_id", playerID)
        gameDetails.put("game_id", gameID)
        gameDetails.put("chat", false)


        val response = ResponseListener<JSONObject>()

        val emitter = Emitter.Listener {
            val data = it[0] as JSONObject
            val move = data.getString("move")
            onMove(Point.fromSGF(move))
        }

        socket.on("game/$gameID/move", emitter)
        socket.on("game/$gameID/gamedata") {
            Log.d("CONNECTED", "${it[0] as JSONObject}")
            response.onResponse(it[0] as JSONObject)
        }
        socket.emit("game/connect", gameDetails)
        return response
    }

    override fun connect(accessToken: String) {
        this.accessToken = accessToken
        socket.connect()
        Log.d("SOCKET CONNECTED", socket.connected().toString())
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