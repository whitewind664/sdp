package com.github.gogetters.letsgo.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.testUtil.TestUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.BeforeClass

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest: EmulatedFirebaseTest() {

    @Test
    fun sharedLocationDoesntAppearInGetAllLocations() {
        TestUtils.makeSureTestUserAuthenticated()
        val location1 = LatLng(1.1, 2.2)
        val location2 = LatLng(3.3, 4.4)
        val user1 = LetsGoUser("id1")
        val user2 = LetsGoUser("id2")
        Tasks.await(user1.shareLocation(location1))
        Tasks.await(user2.shareLocation(location2))
        Database.getAllLocations().thenAccept {
            assertTrue(it.containsKey(location1))
            assertTrue(it.containsKey(location2))
            assertFalse(it.containsValue(Authentication.getUid()!!))
        }
    }

    @Test
    fun basicWriteAndReadWorks() {
        val path = "/test/the/function"
        Tasks.await(Database.writeData(path, true))
        Database.readData(path).continueWith {
            assertTrue(it.result.value != null)
            assertTrue(it.result.value as Boolean)
        }
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
        Tasks.await(Database.writeData("/test/test/test", "test"))
        Tasks.await(Database.readData("/test/test/test"))
        Tasks.await(Database.deleteData("/test/test/test"))
    }

    // ---- [START} Matchmaking  ----
    @Test
    fun matchmakingPairsTwoPlayers() {
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