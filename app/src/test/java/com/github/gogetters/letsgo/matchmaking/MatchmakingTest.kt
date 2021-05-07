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
        val rating1 = 1200.0
        val rating2 = 1000.0

        val (newRating1, newRating2) = Matchmaking.eloChange(rating1, rating2, 1)
        assertTrue(abs(newRating1 - 1207.2) < 0.1)
        assertTrue(abs(newRating2 - 992.8) < 0.1)
    }

    @Test
    fun findMatchWorks() {
        Database.goOffline()

        val auth = Firebase.auth
        auth.signInWithEmailAndPassword("test@test.com", "test")
            .addOnCompleteListener {
                Matchmaking.findMatch {  }

                Database.purgeOutstandingWrites()
                Database.goOnline()
            }
    }
}