package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import junit.framework.Assert.*
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
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityRule.scenario.close()
    }


    @Test
    fun boardExplanationIsShowedSecond() {
        onView(withId(R.id.main_button_startGame)).perform(click())
        onView(withId(R.id.tutorial_textView_explanation)).check(matches(withText("The board")))
    }

    @Test
    fun buttonTextChangesAtEndOfTutorial() {
        /**val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            val textView = activity.findViewById<TextView>(R.id.tutorial_textView_explanation)
            val buttonView = activity.findViewById<Button>(R.id.tutorial_button_next)
            while (textView.text != activity.resources.getString(R.string.tutorial_outro)) {
                onView(withId(R.id.main_button_startGame)).perform(click())
            }

            //assertEquals()
        }*/
    }
}