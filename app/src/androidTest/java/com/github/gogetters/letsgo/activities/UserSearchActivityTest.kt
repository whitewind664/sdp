package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.database.user.FirebaseUserBundleProvider
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.testUtil.TestUtils
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_search.view.*
import org.junit.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class UserSearchActivityTest : EmulatedFirebaseTest() {

    val intent = Intent(ApplicationProvider.getApplicationContext(), UserSearchActivity::class.java)
    lateinit var scenario: ActivityScenario<UserSearchActivity>

    @Before
    fun init() {
        Intents.init()
        scenario = ActivityScenario.launch(intent)
        TestUtils.makeSureTestUserAuthenticated()
    }

    @After
    fun cleanUp() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun existingMatchingUserIsDisplayed() {
        val nick = "Nicky"
        val otherUser = LetsGoUser("test2")
        otherUser.nick = nick
        Tasks.await(otherUser.uploadUserData())

        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(withId(R.id.user_search_search_view)).perform(TestUtils.typeSearchViewText("Nic"))
        TestUtils.sleep()
        val foundUser: UiObject = device.findObject(
            UiSelector().textContains(nick)
        )
        assertTrue(foundUser.exists())
    }

    @Test
    fun existingUnmatchingUserIsNotDisplayed() {
        val nick = "Nicky"
        val otherUser = LetsGoUser("test2")
        otherUser.nick = nick
        Tasks.await(otherUser.uploadUserData())

        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(withId(R.id.user_search_search_view)).perform(TestUtils.typeSearchViewText("Random"))
        TestUtils.sleep()
        val foundUser: UiObject = device.findObject(
            UiSelector().textContains(nick)
        )
        assertFalse(foundUser.exists())
    }

    @Test
    fun canSendFriendRequestToFoundUser() {
        val nick = "Nicky"
        val otherUser = LetsGoUser("test2")
        otherUser.nick = nick
        Tasks.await(otherUser.uploadUserData())

        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(withId(R.id.user_search_search_view)).perform(TestUtils.typeSearchViewText("Nic"))
        TestUtils.sleep()
        val foundUser: UiObject = device.findObject(
            UiSelector().textContains(nick)
        )
        assertTrue(foundUser.exists())
        foundUser.click()
        val friendRequestOption = device.findObject(
            UiSelector().clickable(true).textContains("Friend Request")
        )
        assertTrue(friendRequestOption.exists())
        // TODO maybe click on it and check with mockk that the correct function on user was called
    }

    @Ignore
    @Test
    fun friendIsNotDisplayed() {
        val user = FirebaseUserBundleProvider.getUserBundle()!!.getUser()

        val otherNick = "Nicky"
        val otherUser = LetsGoUser("test2")
        otherUser.nick = otherNick
        Tasks.await(otherUser.uploadUserData())

        // add as a friend
        user.acceptFriend(otherUser)

        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(withId(R.id.user_search_search_view)).perform(TestUtils.typeSearchViewText("Nic"))
        TestUtils.sleep()
        val foundUser: UiObject = device.findObject(
            UiSelector().textContains(otherNick)
        )
        assertFalse(foundUser.exists())
    }

}