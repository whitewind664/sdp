package com.github.gogetters.letsgo.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameModeChooserActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        Intents.init()

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
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
    fun localButtonOpensGame() {
        onView(ViewMatchers.withId(R.id.gameModeChooser_button_local)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(GameActivity::class.java.name))
    }

    @Test
    fun bluetoothButtonOpensBluetooth() {
        onView(ViewMatchers.withId(R.id.gameModeChooser_button_bluetooth)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(BluetoothActivity::class.java.name))
    }

    @Test
    fun ogsButtonOpensLogin() {
        onView(ViewMatchers.withId(R.id.gameModeChooser_button_ogs)).perform(ViewActions.click())
        // TODO
    }

}