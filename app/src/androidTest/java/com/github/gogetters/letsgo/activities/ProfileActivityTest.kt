package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.database.user.FirebaseUserBundleProvider
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.testUtil.TestUtils
import com.github.gogetters.letsgo.testUtil.TestUtils.Companion.clickWaitButton
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileActivityTest : EmulatedFirebaseTest() {

    val intent = Intent(
        ApplicationProvider.getApplicationContext(),
        ProfileActivity::class.java
    )

    lateinit var scenario: ActivityScenario<ProfileActivity>


    @Before
    fun init() {
        Intents.init()
    }

    private fun lateInit() {
        TestUtils.makeSureTestUserAuthenticated()
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
    }


    @After
    fun cleanUp() {
        Intents.release()
        scenario.close()
    }


    @Test
    fun editButtonOpenProfileEdit() {
        lateInit()

        onView(withId(R.id.profile_button_edit)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(ProfileEditActivity::class.java.name))
    }

    @Test
    fun friendsButtonOpensFriends() {
        lateInit()

        onView(withId(R.id.profile_show_friend_list_button)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(FriendListActivity::class.java.name))
    }

    @Test
    fun searchButtonOpensUserSearch() {
        lateInit()

        onView(withId(R.id.profile_search_users_button)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(UserSearchActivity::class.java.name))
    }

    @Test
    fun pressingBackGoesToMainActivity() {
        lateInit()

        onView(isRoot()).perform(ViewActions.pressBack())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun testDisplayUser() {
        TestUtils.makeSureTestUserAuthenticated()
        val user = LetsGoUser(Authentication.getUid()!!)
        val nick = "a_nick"
        val first = "b_first"
        val last = "c_last"
        val country = "d_country"
        val city = "e_city"
        user.nick = nick
        user.first = first
        user.last = last
        user.country = country
        user.city = city
        Tasks.await(user.uploadUserData())

        lateInit()

        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val foundNick: UiObject = device.findObject(
            UiSelector().textContains(nick)
        )
        assertTrue(foundNick.exists())
        val foundCity: UiObject = device.findObject(
            UiSelector().textContains(city)
        )
        assertTrue(foundCity.exists())
        val foundCountry: UiObject = device.findObject(
            UiSelector().textContains(country)
        )
        assertTrue(foundCountry.exists())
    }

    //
    @Test
    fun openLoginWhenNotLoggedIn() {
        Authentication.signOut()
        TestUtils.sleep()
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
        TestUtils.sleep()
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }
}