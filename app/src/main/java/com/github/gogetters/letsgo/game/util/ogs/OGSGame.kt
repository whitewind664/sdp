package com.github.gogetters.letsgo.game.util.ogs

import java.util.*


data class OGSGame(
        val name: String,
        val rules: RuleType,
        val isRanked: Boolean,
        val handicap: HandicapType
)
{
    enum class RuleType {
        JAPANESE, CHINESE;

        override fun toString(): String {
            return this.name.toLowerCase(Locale.ROOT)
        }
    }

    enum class HandicapType(val representation: Int) {
        AUTOMATIC(-1), NONE(0), FIXED(1);

        override fun toString(): String {
            return this.representation.toString()
        }
    }

    override fun toString(): String {
        //TODO to JSON
        return super.toString()
    }
}