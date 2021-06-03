package com.github.gogetters.letsgo.database

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class LetsGoUserTest : EmulatedFirebaseTest() {

    private val TAG = "FirestoreTest"
    private val TEST_UID = "tESTuID"
    private val TEST_UID2 = "tESTuID2"
    private val TEST_UID3 = "tESTuID3"

    private var user = LetsGoUser("")
    private var user2 = LetsGoUser("")
    private var user3 = LetsGoUser("")

    @get:Rule
    var exceptionRule: ExpectedException = ExpectedException.none()

    @Before
    fun initialize() {
        user = LetsGoUser(TEST_UID)
        user2 = LetsGoUser(TEST_UID2)
        user3 = LetsGoUser(TEST_UID3)
    }

    @Test
    fun aUploadedUserDataIsDownloadedCorrectly() {
        val nick = "testerDude"
        val first = "John"
        val last = "Donut"
        val city = "Springfield"
        val country = "USA"
        val isLookingForPlayers = true
        val picRef = "picture1234"
        val lastPosition = LatLng(2.333, 4.3543)
        user.nick = nick
        user.first = first
        user.last = last
        user.city = city
        user.country = country
        user.profileImageRef = picRef
        user.isLookingForPlayers = isLookingForPlayers
        user.lastPositionLongitude = lastPosition.longitude
        user.lastPositionLatitude = lastPosition.latitude

        Tasks.await(user.uploadUserData())
        Tasks.await(user.downloadUserData())

        assertEquals(nick, user.nick)
        assertEquals(first, user.first)
        assertEquals(last, user.last)
        assertEquals(city, user.city)
        assertEquals(country, user.country)
        assertEquals(isLookingForPlayers, user.isLookingForPlayers)
        assertEquals(lastPosition.latitude, user.lastPositionLatitude)
        assertEquals(lastPosition.longitude, user.lastPositionLongitude)
        assertEquals(picRef, user.profileImageRef)
    }

    @Test
    fun aDataChangeIsDownloadedCorrectly() {
        val nick = "testerDude"
        val nick2 = "newTesterDudley"
        val first = "John"
        val last = "Donut"
        val city = "Springfield"
        val country = "USA"
        val isLookingForPlayers = true
        val isLookingForPlayers2 = false
        val picRef = "picture1234"
        val lastPosition = LatLng(2.333, 4.3543)
        val lastPosition2 = LatLng(1.22, 3.23334)
        user2.nick = nick
        user2.first = first
        user2.last = last
        user2.city = city
        user2.country = country
        user2.profileImageRef = picRef
        user2.isLookingForPlayers = isLookingForPlayers
        user2.lastPositionLongitude = lastPosition.longitude
        user2.lastPositionLatitude = lastPosition.latitude

        Tasks.await(user2.uploadUserData())
        Tasks.await(user2.downloadUserData())

        // change some data
        user2.nick = nick2
        user2.isLookingForPlayers = isLookingForPlayers2
        user2.lastPositionLatitude = lastPosition2.latitude
        user2.lastPositionLongitude = lastPosition2.longitude

        Tasks.await(user2.uploadUserData())
        Tasks.await(user2.downloadUserData())

        assertEquals(nick2, user2.nick)
        assertEquals(isLookingForPlayers2, user2.isLookingForPlayers)
        assertEquals(lastPosition2.latitude, user2.lastPositionLatitude)
        assertEquals(lastPosition2.longitude, user2.lastPositionLongitude)
    }


    private fun uploadUserData3() {
        user3.nick = "testerMonkey"
        user3.first = "Marcel"
        user3.last = "Banana"
        user3.city = "Somewhere"
        user3.country = "Africa"

        Tasks.await(user3.uploadUserData())
    }

    @Test
    fun aUserIsActuallyDeleted() {
        user3.profileImageRef = null
        Tasks.await(user3.deleteUserData())
        assertNull(user3.nick)
        assertNull(user3.lastPositionLatitude)
    }

    @Test
    fun aListFriendsByStatusThrowsOnNullFriends() {
        exceptionRule.expect(IllegalStateException::class.java)
        user.friends = null
        user.listFriendsByStatus(LetsGoUser.FriendStatus.ACCEPTED)
    }

    @Test
    fun cTestRequestFriend() {
        Tasks.await(user.requestFriend(user2))
    }

    @Test
    fun cTestAcceptFriend() {
        uploadUserData3()
        Tasks.await(user2.acceptFriend(user3))
        Tasks.await(user3.acceptFriend(user))
    }

    @Test
    fun dTestDeleteFriend() {
        Tasks.await(user3.deleteFriend(user))
    }


    @Test
    fun eTestDownloadFriends() {
        Tasks.await(user2.downloadFriends())

        Log.d(TAG, "------------------------------------------------------------")
        Log.d(TAG, "--- Friends by FriendStatus --------------------------------")
        for (status in LetsGoUser.FriendStatus.values()) {
            Log.d(TAG, "status=$status \t${user2.listFriendsByStatus(status)}")
        }
        Log.d(TAG, "------------------------------------------------------------")
    }

    @Test
    fun aaDeleteUserNotCallingStorageOnNull() {
        mockkObject(Database)
        mockkObject(CloudStorage)

        every { Database.disableLocationSharing() } returns true

        val uid = "0"
        val user = LetsGoUser(uid)
        user.profileImageRef = null
        user.deleteUserData()
        verify(exactly = 0) { CloudStorage.deleteFile("") }
    }

    @Test
    fun aaProfilePictureIsDeletedOnDeleteUserData() {
        mockkObject(Database)
        mockkObject(CloudStorage)
        val uid = "0"
        val ref = "ref"
        val user = LetsGoUser(uid)
        user.profileImageRef = ref
        user.deleteUserData()
        verify(exactly = 1) { CloudStorage.deleteFile(ref) }
    }

    @Ignore("We can remove this later")
    @Test
    fun zTestUploadMichaelUserData() {
        val my_user = LetsGoUser("WOs2S7EDiHRrXZrmvEAr7zV8Awk2")

        my_user.nick = "metaTinker"
        my_user.first = "Michael"
        my_user.last = "Roust"
        my_user.city = "Lausanne"
        my_user.country = "Switzerland"

        Tasks.await(my_user.uploadUserData())
    }

    @Ignore("We can remove this later")
    @Test
    fun zTestMichaelAddFriends() {
        val my_user = LetsGoUser("WOs2S7EDiHRrXZrmvEAr7zV8Awk2")

        Tasks.await(
            Tasks.whenAll(
                my_user.requestFriend(user),
                my_user.acceptFriend(user2),
                my_user.acceptFriend(user3)
            )
        )
    }

//    @Ignore("Make this test work later")
    @Test
    fun zTestSearchUser() {
//        val

        // TODO Add some users with nicknames starting with tester and check that we indeed get them all (use users.length)
        val users = Tasks.await(user.downloadUsersByNick("tester"))
        Log.d(TAG, "Found Users : $users")
    }

    //========================================================================
    // New Tests

    fun addTester1() {
        user.apply {
            first = "John"
            last = "Lane"
            city = "a"
            country = "b"
            nick = "testerDudeX"
        }

        Tasks.await(user.uploadUserData())
    }

    fun addTester2() {
        user2.apply {
            first = "Alice"
            last = "Surf"
            city = "zh"
            country = "ch"
            nick = "testerGalX"
        }

        Tasks.await(user2.uploadUserData())
    }

    fun addTester3() {
        user3.apply {
            first = "Bob"
            last = "The builder"
            city = "blockcity"
            country = "blockland"
            nick = "weCanFixIt"
        }

        Tasks.await(user3.uploadUserData())
    }

    @Test
    fun testUploadUserDataOffline() {
        Firebase.database.goOffline()
        Tasks.await(user.uploadUserData())
        Firebase.database.goOnline()
    }

    @Test
    fun testDownloadUserDataOffline() {
        Firebase.database.goOffline()
        Tasks.await(user.downloadUserData())
        Firebase.database.goOnline()
    }

    @Test
    fun testDeleteUserDataOffline() {
        Firebase.database.goOffline()
        Tasks.await(user.deleteUserData())
        Firebase.database.goOnline()
    }

    @Test
    fun testDeleteFriend() {
        Firebase.database.goOffline()
        Tasks.await(user.deleteFriend(user2))
        Firebase.database.goOnline()
    }


}