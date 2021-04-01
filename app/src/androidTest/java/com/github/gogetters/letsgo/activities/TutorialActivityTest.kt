package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import junit.framework.Assert.*
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TutorialActivityTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<TutorialActivity> = ActivityScenarioRule(
            Intent(
                    ApplicationProvider.getApplicationContext(),
                    TutorialActivity::class.java
            )
    )

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
    fun boardExplanationIsShowedSecond() {
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_textView_explanation)).check(matches(withText(containsString("The board"))))
    }

    @Test
    fun buttonTextChangesAtEndOfTutorial() {
        for (i in 1..20) {
            onView(withId(R.id.tutorial_button_next)).perform(click()) // TODO do it the correct number of times
            Thread.sleep(5)
        }
        onView(withId(R.id.tutorial_button_next)).check(matches(withText(containsString("Back"))))
    }

}