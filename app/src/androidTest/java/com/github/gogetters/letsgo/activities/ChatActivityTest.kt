package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.database.user.LetsGoUser
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatActivityTest : EmulatedFirebaseTest() {

    val ITEM_IN_TEST = 0
    val USER_IN_TEST = LetsGoUser("id")

    val intent = Intent(ApplicationProvider.getApplicationContext(), ChatActivity::class.java).putExtra(
        ChatNewMessageActivity.KEY,
        USER_IN_TEST
    )
    lateinit var scenario: ActivityScenario<ChatActivity>


    @Before
    fun init() {
        Intents.init()
        scenario = ActivityScenario.launch(intent)
        USER_IN_TEST.nick = "nick"
        USER_IN_TEST.first = "first"
        USER_IN_TEST.last = "last"
        USER_IN_TEST.city = "city"
        USER_IN_TEST.country = "country"

    }

    @After
    fun cleanUp() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun test_isItemsVisible_onAppLaunch() {
        onView(withId(R.id.chat_recyclerview_messages)).check(
            matches(
                isDisplayed()
            )
        )
        onView(withId(R.id.chat_editText_input)).check(matches(isDisplayed()))
        onView(withId(R.id.chat_send_button)).check(matches(isDisplayed()))
    }

    @Test
    fun sentMessageIsAdded() {
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val message = "Time: $ts"
        onView(withId(R.id.chat_editText_input)).perform(ViewActions.typeText(message))
        onView(withId(R.id.chat_send_button)).perform(click())
        onView(withText(message)).check(matches(isDisplayed()))
    }
}
