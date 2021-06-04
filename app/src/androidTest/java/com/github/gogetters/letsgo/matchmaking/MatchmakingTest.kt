package com.github.gogetters.letsgo.matchmaking;

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.matchmaking.Matchmaking
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.*
import org.junit.runner.RunWith
import kotlin.math.abs


@RunWith(AndroidJUnit4::class)
public class MatchmakingTest: EmulatedFirebaseTest() {

    init {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        FirebaseApp.initializeApp(appContext)
    }

    @Test
    fun findMatchWorks() {

    }

    @Test
    fun eloChangeReturnsExpectedNumbers() {
        val rating1 = 1500.0
        val rating2 = 1500.0

        val (newRating1, newRating2) = Matchmaking.eloChange(rating1, rating2, 1)
        print(newRating1)
        print(newRating2)
        Assert.assertTrue(abs(newRating1 - 1508) < 0.1)
        Assert.assertTrue(abs(newRating2 - 1492) < 0.1)
    }
}
