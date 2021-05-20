package com.github.gogetters.letsgo.game.util.ogs

import org.json.JSONObject
import java.util.*


data class OGSGame(
    val name: String,
    val width: Int = 9,
    val height: Int = 9
)
{

    fun toJSON(): JSONObject {
        val game = JSONObject()
        game.put("name", name)
        game.put("rules", "japanese")
        game.put("ranked", "false")
        game.put("handicap", 0)
        game.put("time_control_parameters", JSONObject().put("time_control", "none"))
        game.put("pause_on_weekends", "false")
        game.put("width", width)
        game.put("height", height)
        game.put("disable_analysis", "false")
        return game
    }

    override fun toString(): String {
        return toJSON().toString(4)
    }
}