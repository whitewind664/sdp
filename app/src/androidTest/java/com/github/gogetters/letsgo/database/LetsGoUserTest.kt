package com.github.gogetters.letsgo.database

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class LetsGoUserTest: EmulatedFirebaseTest() {

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
    fun aTestUploadUserData() {
        user.nick = "testerDude"
        user.first = "John"
        user.last = "Donut"
        user.city = "Springfield"
        user.country = "USA"

        Tasks.await(user.uploadUserData())
    }

    @Test
    fun aTestUploadUserData2() {
        user2.nick = "testerGirl"
        user2.first = "Mary"
        user2.last = "Pretzel"
        user2.city = "DoesntExist"
        user2.country = "Australia"

        Tasks.await(user2.uploadUserData())
    }

    @Test
    fun aTestUploadUserData3() {
        user3.nick = "testerMonkey"
        user3.first = "Marcel"
        user3.last = "Banana"
        user3.city = "Somewhere"
        user3.country = "Africa"

        Tasks.await(user3.uploadUserData())
    }

    @Test
    fun bTestDownloadUserData() {
        Tasks.await(user.downloadUserData())
    }

    @Test
    fun bDeleteUserData() {
        Tasks.await(user.deleteUserData())

        aTestUploadUserData() // Reupload after delete
    }

    @Test
    fun cTestRequestFriend() {
        Tasks.await(user.requestFriend(user2))
    }

    @Test
    fun cTestAcceptFriend() {
        Tasks.await(user2.acceptFriend(user3))
        Tasks.await(user3.acceptFriend(user))
    }

    @Test
    fun dTestDeleteFriend() {
        Tasks.await(user3.deleteFriend(user))
    }

    @Test
    fun eTestCheckUserExists() {
        Tasks.await(user.requireUserExists())
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

}