package com.github.gogetters.letsgo.activities

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.database.LetsGoUserTest
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.testUtil.TestUtils
import com.google.android.gms.tasks.Tasks
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FriendListActivityTest: EmulatedFirebaseTest() {

    @get:Rule
    var activityRule = ActivityScenarioRule(FriendListActivity::class.java)

    @After
    fun cleanUp() {
        activityRule.scenario.close()
    }

    @Test
    fun friendListTest() {
        val helper = LetsGoUserTest()
        helper.initialize()

        helper.addTester1()
        helper.addTester2()

        TestUtils.makeSureTestUserAuthenticated()
        val testUid = Authentication.getCurrentUser()!!.uid
        val testUser = LetsGoUser(testUid)

        Tasks.await(testUser.acceptFriend(helper.user))
        Tasks.await(testUser.requestFriend(helper.user2))

        activityRule.scenario.recreate()

        // TODO Maybe test the screen contains the nicknames of helper.user and helper.user2?
    }
}