package com.github.gogetters.letsgo.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LetsGoUserTest {

    private val TAG = "FirestoreTest"
    private val TEST_UID = "tESTuID"

    var user = LetsGoUser("")

    @Before
    fun initialize() {
        user = LetsGoUser(TEST_UID)
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
}