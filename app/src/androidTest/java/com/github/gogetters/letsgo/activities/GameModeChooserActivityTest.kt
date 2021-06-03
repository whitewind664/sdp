package com.github.gogetters.letsgo.activities

import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.ogs.*
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.IllegalStateException

@RunWith(AndroidJUnit4::class)
class GameModeChooserActivityTest: EmulatedFirebaseTest() {
    @get:Rule
    var activityRule = ActivityScenarioRule(GameModeChooserActivity::class.java)

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

    @Test
    fun connectionTest() { //TODO remove cause it depends on 3rd party interface
        Log.d("TEST TEST TEST", "here we are not in the activity")
        activityRule.scenario.onActivity { activity ->
            val ogs = OGSCommunicatorService(
                    VolleyOnlineService(activity),
                    SocketIOService(),
                    activity.resources.getString(R.string.ogs_client_id),
                    activity.resources.getString(R.string.ogs_client_secret))

            ogs.authenticate("john", "doe").setOnResponse {
                if (it) {
                    ogs.startChallenge()
                } else {
                    throw IllegalStateException("FAILED TO AUTHENTICATE")
                }
            }

        }
    }

}