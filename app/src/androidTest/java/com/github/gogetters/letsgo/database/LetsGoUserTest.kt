package com.github.gogetters.letsgo.database

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LetsGoUserTest {

    private val TAG = "FirestoreTest"
    private val TEST_UID = "tESTuID"
    private val TEST_UID2 = "tESTuID2"
    private val TEST_UID3 = "tESTuID3"

    private var user = LetsGoUser("")
    private var user2 = LetsGoUser("")
    private var user3 = LetsGoUser("")

    @Before
    fun initialize() {
        user = LetsGoUser(TEST_UID)
        user2 = LetsGoUser(TEST_UID2)
        user3 = LetsGoUser(TEST_UID3)

        // Once mock database is implemented we can use this!
        // val user = LetsGoUser(TEST_UID, <MockDatabase>)
    }

    @Test
    fun testUploadUserData() {
        user.nick = "testerDude"
        user.first = "John"
        user.last = "Donut"
        user.city = "Springfield"
        user.country = "USA"

        Tasks.await(user.uploadUserData())
    }

    @Test
    fun testUploadUserData2() {
        user2.nick = "testerGirl"
        user2.first = "Mary"
        user2.last = "Pretzel"
        user2.city = "DoesntExist"
        user2.country = "Australia"

        Tasks.await(user2.uploadUserData())
    }

    @Test
    fun testUploadUserData3() {
        user3.nick = "testerMonkey"
        user3.first = "Marcel"
        user3.last = "Banana"
        user3.city = "Somewhere"
        user3.country = "Africa"

        Tasks.await(user3.uploadUserData())
    }

    @Test
    fun testDownloadUserData() {
        Tasks.await(user.downloadUserData())
    }

    @Test
    fun deleteUserData() {
        Tasks.await(user.deleteUserData())

        testUploadUserData()
    }


    // TODO Remove this later
    @Test
    fun testUploadMichaelUserData() {
        val my_user = LetsGoUser("WOs2S7EDiHRrXZrmvEAr7zV8Awk2")

        my_user.nick = "metaTinker"
        my_user.first = "Michael"
        my_user.last = "Roust"
        my_user.city = "Lausanne"
        my_user.country = "Switzerland"

        Tasks.await(my_user.uploadUserData())
    }


    // TODO Remove this
    @Test
    fun testUploadFriendListScratch() {
//        val my_uid = "WOs2S7EDiHRrXZrmvEAr7zV8Awk2"
//        val my_user = LetsGoUser(my_uid)

//        Tasks.await(my_user.downloadUserData())

        val db = Firebase.database

        val friendListRef = db.getReference("users/${user.uid}/friends")
        val newFriendRef = friendListRef.push()

        Tasks.await(newFriendRef.setValue(TEST_UID2));

        Log.d(TAG, "Wow!: $user")
    }

    @Test
    fun testRequestFriend() {
        Tasks.await(user.requestFriend(user2))
    }

    @Test
    fun testAcceptFriend() {
        Tasks.await(user2.acceptFriend(user3))
    }

    @Test
    fun testCheckUserExists() {
        Tasks.await(user.requireUserExists())
    }

    @Test
    fun testDownloadFriends() {
        Tasks.await(user2.downloadFriends())

        Log.d(TAG, "------------------------------------------------------------")
        Log.d(TAG, "--- Friends by FriendStatus --------------------------------")
        for (status in LetsGoUser.FriendStatus.values()) {
            Log.d(TAG, "status=$status \t${user2.listFriendsByStatus(status)}")
        }
        Log.d(TAG, "------------------------------------------------------------")
    }

    @Test
    fun deleteFriend() {
        Tasks.await(user.deleteFriend(user2))
    }
}