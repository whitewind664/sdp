package com.github.gogetters.letsgo.database

import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.junit.Before
import org.junit.BeforeClass

abstract class EmulatedFirebaseTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun databaseSetup() {
            if (!Database.isEmulated) {
                val appContext = InstrumentationRegistry.getInstrumentation().targetContext
                FirebaseApp.initializeApp(appContext)
                Database.emulatorSettings()

//                Firebase.auth.useEmulator("10.0.2.2", 9099)

                Firebase.storage.useEmulator("10.0.2.2", 9199)
            }
        }
    }

    @Before
    fun cleanDatabase() {
        Database.flushRealtimeDatabase()
    }
}