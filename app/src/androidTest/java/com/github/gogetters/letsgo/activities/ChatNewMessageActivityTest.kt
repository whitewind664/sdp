package com.github.gogetters.letsgo.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.model.UserData
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ChatNewMessageActivityTest: EmulatedFirebaseTest() {

    val ITEM_IN_TEST = 0
    val USER_IN_TEST = UserData("id", "nick", "first", "last", "city", "country")

    @get:Rule
    var activityRule = ActivityScenarioRule(ChatNewMessageActivity::class.java)

    @Before
    fun init() {
        Intents.init()
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityRule.scenario.close()
    }

    @Ignore
    @Test
    fun test_isItemsVisible_onAppLaunch() {
        onView(ViewMatchers.withId(R.id.chat_recyclerview_new_message)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    /*
    @Test
    fun test_selectItem_isChatNewMessageActivityVisible() {
        onView(withId(R.id.chat_recyclerview_last_message))
            .perform(actionOnItemAtPosition<ViewHolder>(ITEM_IN_TEST, click()))

        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasComponent(
                    ChatActivity::class.java.name
                )
            )
        )

    }
    */

}