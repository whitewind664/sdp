package com.github.gogetters.letsgo.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.testUtil.TestUtils
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameModeChooserActivityTest: EmulatedFirebaseTest() {
    @get:Rule
    var activityRule = ActivityScenarioRule(GameModeChooserActivity::class.java)

    @Before
    fun init() {
        Intents.init()

        TestUtils.clickWaitButton()
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityRule.scenario.close()
    }


    @Test
    fun localButtonOpensGame() {
        onView(withId(R.id.gameModeChooser_button_local)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(GameActivity::class.java.name))
    }

    @Test
    fun bluetoothButtonOpensBluetooth() {
        onView(withId(R.id.gameModeChooser_button_bluetooth)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(BluetoothActivity::class.java.name))
    }

    @Test
    fun ogsButtonOpensLogin() {
        onView(withId(R.id.gameModeChooser_button_ogs)).perform(ViewActions.click())
        onView(withId(R.id.gameModeChooser_editText_loginUsername)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        onView(withId(R.id.gameModeChooser_editText_loginPassword)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}