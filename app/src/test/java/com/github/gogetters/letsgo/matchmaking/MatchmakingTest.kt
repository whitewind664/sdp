package com.github.gogetters.letsgo.matchmaking

import com.github.gogetters.letsgo.database.Database
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.Test
import org.junit.Assert.assertTrue
import kotlin.math.abs

class MatchmakingTest {

    @Test
    fun eloChangeReturnsExpectedNumbers() {
        val rating1 = 1500.0
        val rating2 = 1500.0

        val (newRating1, newRating2) = Matchmaking.eloChange(rating1, rating2, 1)
        assertTrue(abs(newRating1 - 1508) < 0.1)
        assertTrue(abs(newRating2 - 1492) < 0.1)
    }
}