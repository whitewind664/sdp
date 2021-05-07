package com.github.gogetters.letsgo.activities;

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.matchmaking.Matchmaking
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
public class MatchmakingTest {

    init {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        FirebaseApp.initializeApp(appContext)
    }

    @Test
    fun findMatchWorks() {
        Database.goOffline()

        val auth = Firebase.auth
        auth.signInWithEmailAndPassword("test@test.com", "password")
                .addOnCompleteListener {
                    Matchmaking.findMatch {  }

                    Database.purgeOutstandingWrites()
                    Database.goOnline()
                }
    }
}
