package com.github.gogetters.letsgo.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
    companion object {
        init {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            FirebaseApp.initializeApp(appContext)
            Database.emulatorSettings()
        }
    }

    @Test
    fun disableLocationSharingDoesntThrow() {
        Database.disableLocationSharing()
    }

    @Test
    fun getAllLocationsDoesntThrow() {
        Database.getAllLocations()
    }

    @Test
    fun listenToMessages() {

        val chatId = "fakeChatId"

        val listener = Database.addMessagesListener(chatId) {
            // do nothing
        }

        // TODO write database functions with await instead of callbacks if possible

        Database.sendMessage("fakeSenderId", chatId, "fakeText", {

        }, {

        })

        Database.removeMessagesListener(chatId, listener)

    }

    @Test
    fun writeValue() {
        Database.flushRealtimeDatabase()

        var done = 0

        Database.writeValue("fakepath", "fakeval", {
            done = 1
        }, {
            done = 2
        })

        runBlocking {
            while (done == 0) {
                delay(1000)
            }
        }

        assertEquals(1, done)

    }

    @Test
    fun refFunctions() {
        Database.flushRealtimeDatabase()

        Tasks.await(Database.writeData("/test/test/test", "test"))
        Tasks.await(Database.readData("/test/test/test"))
        Tasks.await(Database.deleteData("/test/test/test"))
    }

    // ---- [START} Matchmaking  ----
    @Test
    fun matchmakingPairsTwoPlayers() {
        Database.flushRealtimeDatabase()

        var canContinue = false

        Database.findMatch("fakePlayer1", 1) { _, _, _ ->
            canContinue = true
        }

        // TODO find a way of modularizing this
        runBlocking {
            while (!canContinue) {
                delay(1000)
            }
        }

        val x = Tasks.await(Database.readData("/matchmaking/currentlyWaiting")).value
        assertEquals("fakePlayer1", x)

        canContinue = false

        Database.findMatch("fakePlayer2", 2) { _, _, _ ->
            canContinue = true
        }

        runBlocking {
            while (!canContinue) {
                delay(1000)
            }
        }

        val y = Tasks.await(Database.readData("/matchmaking/currentlyWaiting")).value
        assertEquals(null, y)
    }

    // ---- [END} Matchmaking  ----
}