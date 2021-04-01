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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        Intents.init()
        activityRule.scenario.onActivity { activity ->
            activity.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        }
        Thread.sleep(5000)
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityRule.scenario.close()
    }


    @Test
    fun gameButtonOpensGame() {
        onView(withId(R.id.main_button_startGame)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(GameActivity::class.java.name))
    }

    @Test
    fun profileButtonOpensProfile() {
        onView(withId(R.id.main_button_profile)).perform(click())
        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(ProfileActivity::class.java.name)))
    }
}