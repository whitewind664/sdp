package com.github.gogetters.letsgo.game.util.ogs

import org.json.JSONObject
import java.util.*


data class OGSGame(
    val name: String,
    val rules: RuleType = RuleType.CHINESE,
    val isRanked: Boolean = false,
    val handicap: HandicapType = HandicapType.AUTOMATIC,
    val timeControl: TimeControl = TimeControl.None(3600),
    val pauseOnWeekends: Boolean = false,
    val width: Int = 9,
    val height: Int = 9,
    val disableAnalysis: Boolean = false
)
{

    fun toJSON(): JSONObject {
        val game = JSONObject()
        game.put("name", name)
        game.put("rules", rules)
        game.put("ranked", isRanked.toString().toLowerCase(Locale.ROOT))
        game.put("handicap", handicap)
        game.put("time_control_parameters", timeControl.toJSON())
        game.put("pause_on_weekends", pauseOnWeekends.toString().toLowerCase(Locale.ROOT))
        game.put("width", width)
        game.put("height", height)
        game.put("disable_analysis", disableAnalysis)
        return game
    }

    override fun toString(): String {
        return toJSON().toString(4)
    }


    enum class RuleType {
        JAPANESE, CHINESE;

        override fun toString(): String {
            return this.name.toLowerCase(Locale.ROOT)
        }
    }

    enum class HandicapType(private val representation: Int) {
        AUTOMATIC(-1), NONE(0), FIXED(1);

        override fun toString(): String {
            return this.representation.toString()
        }
    }

    abstract class TimeControl {
        abstract fun toJSON(): JSONObject

        override fun toString(): String {
            return toJSON().toString(4)
        }

        data class Fischer(val initialTime: Int, val maxTime: Int, val timeIncrement: Int): TimeControl() {
            override fun toJSON(): JSONObject {
                val timeControl = JSONObject()
                timeControl.put("time_control", "fischer")
                timeControl.put("initial_time", initialTime)
                timeControl.put("max_time", maxTime)
                timeControl.put("time_increment", timeIncrement)
                return timeControl
            }
        }

        data class Byoyomi(val mainTime: Int, val periodTime: Int) : TimeControl() {
            override fun toJSON(): JSONObject {
                val timeControl = JSONObject()
                timeControl.put("time_control", "byoyomi")
                timeControl.put("main_time", mainTime)
                timeControl.put("period_time", periodTime)
                return timeControl
            }
        }

        data class Simple(val perMove: Int): TimeControl() {
            override fun toJSON(): JSONObject {
                val timeControl = JSONObject()
                timeControl.put("time_control", "simple")
                timeControl.put("per_move", perMove)
                return timeControl
            }
        }

        data class Canadian(val mainTime: Int, val periodTime: Int, val stonesPerPeriod: Int) : TimeControl() {
            override fun toJSON(): JSONObject {
                val timeControl = JSONObject()
                timeControl.put("time_control", "byoyomi")
                timeControl.put("main_time", mainTime)
                timeControl.put("period_time", periodTime)
                timeControl.put("stones_per_period", stonesPerPeriod)
                return timeControl
            }
        }

        data class Absolute(val totalTime: Int): TimeControl() {
            override fun toJSON(): JSONObject {
                val timeControl = JSONObject()
                timeControl.put("time_control", "simple")
                timeControl.put("total_time", totalTime)
                return timeControl
            }
        }

        data class None(val totalTime: Int): TimeControl() {
            override fun toJSON(): JSONObject {
                val timeControl = JSONObject()
                timeControl.put("time_control", "none")
                return timeControl
            }
        }
    }
}