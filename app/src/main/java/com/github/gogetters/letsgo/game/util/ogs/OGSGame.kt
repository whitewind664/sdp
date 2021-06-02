package com.github.gogetters.letsgo.game.util.ogs

import org.json.JSONObject
import java.util.*


data class OGSGame(
        val id: String,
        val name: String,
        val width: Int = 9,
        val height: Int = 9
) {

    fun toJSON(): JSONObject {
        val game = JSONObject()
        val time = JSONObject()
        time.put("time_control", "none")

        game.put("id", id)
        game.put("name", name)
        game.put("rules", "japanese")
        game.put("ranked", "false")
        game.put("handicap", 0)
        game.put("time_control_parameters", time)
        game.put("pause_on_weekends", "false")
        game.put("width", width)
        game.put("height", height)
        game.put("disable_analysis", "false")
        if (id != "") {
            game.put("id", id)
        }
        return game
    }

    override fun toString(): String {
        return toJSON().toString(4)
    }

    companion object {
        fun fromJSON(game: JSONObject): OGSGame {
            return OGSGame(game.getString("id"), game.getString("name"),
                    game.getInt("width"), game.getInt("height"))
        }
    }
}