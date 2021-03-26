package com.github.gogetters.letsgo.activities

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
    @Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun gameButtonOpensGame() {
        Intents.init()

        onView(withId(R.id.main_button_startGame)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(GameActivity::class.java.name))

        Intents.release()
    }

    @Test
    fun profileButtonOpensProfile() {
        Intents.init()

        onView(withId(R.id.main_button_profile)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))

        Intents.release()
    }
}