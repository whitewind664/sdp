package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers
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
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        var waitButton = device.findObject(UiSelector().textContains("wait"))
        if (waitButton.exists()) {
            waitButton.click()
        }
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityRule.scenario.close()
    }

    @Test
    fun boardExplanationIsShowedSecond() {
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_textView_title)).check(matches(withText(containsString("The Board"))))
        onView(withId(R.id.tutorial_textView_explanation)).check(matches(withText(containsString("Go is"))))
    }

    /**
     * Clicks as many times as needed to see the board for the first time
     */
    private fun goToFirstBoard() {
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
    }

    @Test
    fun boardIsDisplayedAfterBoardExplanation() {
        goToFirstBoard()
        onView(withId(R.id.tutorial_frameLayout_boardFrame)).check(matches(isDisplayed()))
    }

    @Test
    fun canPlaceStoneFreelyOnFirstBoard() {
        goToFirstBoard()
        onView(withParent(withId(R.id.tutorial_frameLayout_boardFrame))).perform(touchDownAndUp(1f, 1f))
    }

    @Test
    fun canPlaceStoneFreelyAfterReset() {
        goToFirstBoard()
        onView(withParent(withId(R.id.tutorial_frameLayout_boardFrame))).perform(touchDownAndUp(1f, 1f))
        onView(withId(R.id.tutorial_button_reset)).perform(click())
        onView(withParent(withId(R.id.tutorial_frameLayout_boardFrame))).perform(touchDownAndUp(1f, 1f))
    }

    @Test
    fun goesBackToMainAtEndOfTutorial() {
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        onView(withId(R.id.tutorial_button_next)).perform(click())
        // ATTENTION: the number of clicks matters!
        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(MainActivity::class.java.name)))
    }

    private fun touchDownAndUp(x: Float, y: Float): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Send touch events."
            }

            override fun perform(uiController: UiController, view: View) {
                // Get view absolute position
                val location = IntArray(2)
                view.getLocationOnScreen(location)

                // Offset coordinates by view position
                val coordinates = floatArrayOf(x + location[0], y + location[1])
                val precision = floatArrayOf(1f, 1f)

                // Send down event, pause, and send up
                val down = MotionEvents.sendDown(uiController, coordinates, precision).down
                uiController.loopMainThreadForAtLeast(200)
                MotionEvents.sendUp(uiController, down, coordinates)
            }
        }
    }

}