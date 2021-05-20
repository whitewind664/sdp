package com.github.gogetters.letsgo.game.util.ogs

import com.github.gogetters.letsgo.game.Stone
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.util.*

class TestOnlineService : OnlineService {
    var hasAuthenticated = false
    var currentGames = listOf(OGSGame("default"), OGSGame("ranked", isRanked = true))
    var madeMove = false
    var challenges = listOf<OGSChallenge>()

    fun challengeFromJSON(challenge: JSONObject): OGSChallenge {

        val color = when (challenge.getString("challenger_color")) {
            "white" -> Stone.WHITE
            "black" -> Stone.BLACK
            "automatic" -> Stone.BLACK //todo :(((((((
            else -> throw IllegalArgumentException("could not parse stone color")
        }

        val game = gameFromJSON(challenge.getJSONObject("game"))
        return OGSChallenge(game, color,
            challenge.getInt("min_ranking"),
            challenge.getInt("max_ranking"))
    }

    private fun gameFromJSON(game: JSONObject): OGSGame {

        val rules = when (game.getString("rules")) {
            OGSGame.RuleType.CHINESE.name.toLowerCase(Locale.ROOT) -> OGSGame.RuleType.CHINESE
            OGSGame.RuleType.JAPANESE.name.toLowerCase(Locale.ROOT) -> OGSGame.RuleType.CHINESE
            else -> throw IllegalArgumentException("invalid rule type")
        }

        val handicap = when (game.getString("handicap")) {
            OGSGame.HandicapType.AUTOMATIC.toString() -> OGSGame.HandicapType.AUTOMATIC
            OGSGame.HandicapType.FIXED.toString() -> OGSGame.HandicapType.FIXED
            OGSGame.HandicapType.NONE.toString() -> OGSGame.HandicapType.NONE
            else -> throw IllegalArgumentException("invalid handicap type")
        }

        val timeControlJSON = game.getJSONObject("time_control_parameters")
        val timeControl = when (timeControlJSON.getString("time_control")) {
            "none" -> OGSGame.TimeControl.None(timeControlJSON.getInt("total_time"))
            "byoyomi" -> OGSGame.TimeControl.Byoyomi(timeControlJSON.getInt("main_time"),
            timeControlJSON.getInt("period_time"))
            "fischer" -> OGSGame.TimeControl.Fischer(timeControlJSON.getInt("initial_time"),
            timeControlJSON.getInt("max_time"), timeControlJSON.getInt("time_increment"))
            "simple" -> OGSGame.TimeControl.Simple(timeControlJSON.getInt("per_move"))
            "canadian" -> OGSGame.TimeControl.Canadian(timeControlJSON.getInt("main_time"),
            timeControlJSON.getInt("period_time"), timeControlJSON.getInt("stones_per_period"))
            "absolute" -> OGSGame.TimeControl.Absolute(timeControlJSON.getInt("total_time"))
            else -> throw IllegalArgumentException("could not parse time control")
        }

        return OGSGame(
            game.getString("name"),
            rules,
            game.getBoolean("ranked"),
            handicap,
            timeControl,
            game.getBoolean("pause_on_weekends"),
            game.getInt("width"),
            game.getInt("height"),
            game.getBoolean("disable_analysis")
        )
    }

    override fun post(url: String, body: String): OnlineService.ResponseListener {
        when (url) {

        }
    }

    override fun get(url: String): OnlineService.ResponseListener {
        when (url) {

        }
    }

    override fun delete(url: String): OnlineService.ResponseListener {
        when (url) {

        }
    }

}