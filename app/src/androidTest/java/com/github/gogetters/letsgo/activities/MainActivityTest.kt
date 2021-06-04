package com.github.gogetters.letsgo.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.testUtil.TestUtils
import org.hamcrest.Matchers
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest: EmulatedFirebaseTest() {
    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        Intents.init()

        val device = UiDevice.getInstance(getInstrumentation())
        var waitButton = device.findObject(UiSelector().textContains("wait"))
        if (waitButton.exists()) {
            waitButton.click()
        }
        //Thread.sleep(5000)
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityRule.scenario.close()
    }


    @Test
    fun gameButtonOpensModeChooser() {
        onView(withId(R.id.item1)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(GameModeChooserActivity::class.java.name))
    }

    @Test
    fun profileButtonOpensProfile() {
        onView(withId(R.id.main_button_profile)).perform(click())
        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(ProfileActivity::class.java.name)))
    }

    @Test
    fun mapButtonOpensMap() {
        onView(withId(R.id.main_button_map)).perform(click())
        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(MapsActivity::class.java.name)))
    }

    @Test
    fun chatButtonOpensChat() {
        TestUtils.makeSureTestUserAuthenticated()
        onView(withId(R.id.item3)).perform(click())
        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(ChatLastMessageActivity::class.java.name)))
    }

    @Test
    fun tutorialButtonOpensTutorial() {
        onView(withId(R.id.item2)).perform(click())
        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(TutorialActivity::class.java.name)))
    }

    @Test
    fun playButtonOpensGameChooser() {
        onView(withId(R.id.main_button_play)).perform(click())
        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(GameModeChooserActivity::class.java.name)))
    }
}