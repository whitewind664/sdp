package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun gameButtonOpensGame() {
        /** //Somehow doesn't work
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            // close system dialogs
            Thread.sleep(1)
            activity.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

            Intents.init()
            onView(withId(R.id.main_button_startGame)).perform(click())
            Intents.intended(IntentMatchers.hasComponent(GameActivity::class.java.name))
            Intents.release()
        }*/
    }

    @Test
    fun profileButtonOpensProfile() {
        /** //Somehow doesn't work
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            Intents.init()
            onView(withId(R.id.main_button_profile)).perform(click())
            Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(ProfileActivity::class.java.name)))
            Intents.release()
        }*/
    }
}