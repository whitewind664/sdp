package com.github.gogetters.letsgo.database

import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import org.junit.Before
import org.junit.BeforeClass

abstract class EmulatedFirebaseTest {
    companion object {
        @BeforeClass @JvmStatic
        fun databaseSetup() {
            if (!Database.isEmulated) {
                val appContext = InstrumentationRegistry.getInstrumentation().targetContext
                FirebaseApp.initializeApp(appContext)
                Database.emulatorSettings()

                // Commenting this will use the real FirebaseAuth and not the emulated one
                // We should try to make it work
                Authentication.emulatorSettings()
            }
        }
    }

    @Before
    fun cleanDatabase() {
        Database.flushRealtimeDatabase()
    }
}