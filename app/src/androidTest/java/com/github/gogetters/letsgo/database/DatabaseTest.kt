package com.github.gogetters.letsgo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.EmulatorSuite
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    init {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        FirebaseApp.initializeApp(appContext)
        EmulatorSuite.emulatorSettings()
    }

    @Test
    fun disableLocationSharingDoesntThrow() {
//        Database.goOffline()
        Database.disableLocationSharing()
//        Database.purgeOutstandingWrites()
//        Database.goOnline()
    }

    @Test
    fun getAllLocationsDoesntThrow() {
//        Database.goOffline()
        Database.getAllLocations()
//        Database.purgeOutstandingWrites()
//        Database.goOnline()
    }

    @Test
    fun listenToMessages() {
//        Database.goOffline()

        val chatId = "fakeChatId"

        val listener = Database.addMessagesListener(chatId) {
            // do nothing
        }

        // TODO write database functions with await instead of callbacks if possible

        Database.sendMessage("fakeSenderId", chatId, "fakeText", {
//            Database.purgeOutstandingWrites()
//            Database.goOnline()
        }, {
//            Database.purgeOutstandingWrites()
//            Database.goOnline()
        })

        Database.removeMessagesListener(chatId, listener)

    }

    @Test
    fun writeValue() {
//        Database.goOffline()

        Database.writeValue("asdf", "fakeval", {
//            Database.purgeOutstandingWrites()
//            Database.goOnline()
        }, {
//            Database.purgeOutstandingWrites()
//            Database.goOnline()
        })

    }

//    @Test
//    fun refFunctions() {
//        Database.goOffline()
//
//        Tasks.await(Database.writeData("/test/test/test", "test"))
//        Tasks.await(Database.readData("/test/test/test"))
//        Tasks.await(Database.deleteData("/test/test/test"))
//
//        Database.purgeOutstandingWrites()
//        Database.goOnline()
//
//    }

    // ---- [START} Matchmaking  ----
    @Test
    fun matchmakingPairsTwoPlayers() {
//        Database.goOffline()

        Database.findMatch("fakePlayer1", 1)
//        val x = Tasks.await(Database.readData("/matchmaking/currentlyWaiting")).value
//        assertEquals("fakePlayer1", x)
        Database.findMatch("fakePlayer2", 2)
//        val y = Tasks.await(Database.readData("/matchmaking/currentlyWaiting")).value
//        assertEquals(null, y)

//        Database.purgeOutstandingWrites()
//        Database.goOnline()
    }

    // ---- [END} Matchmaking  ----
}