package com.github.gogetters.letsgo.activities

import android.util.Log
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
import com.github.gogetters.letsgo.game.util.ogs.VolleyOnlineService
import org.json.JSONObject
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
    fun connectionTest() {
        Log.d("TEST TEST TEST", "here we are not in the activity")
        activityRule.scenario.onActivity { activity ->
            Log.d("ACTIVITY ACTIVITY ACTIVITY", "we are inside the activity's thread")
            val service = VolleyOnlineService(activity)
            val url = "https://online-go.com/"
            val auth = "api/v0/login"

            val clientID = "iwEQK38MjhLnudkiY6vJkrk6roGXks4iSA6fE6ln"
            val clientSecret = "8L9EX4cFGKyylq9a5rw4aBo4G0vzCtaWdszQdcAex8xr9dMJ46xSLuKqUf" +
                    "ieKnQydYldq9PgwYeOSsET6XbPLbomniIWh4Bixa5OrIDpfQrpkL7veUiRWz9GFN4NQVRW"

            val body = JSONObject()
            val username = "kimonroxd" //Don't laugh, this is from my spam email i made when i was 12...
            val password = "online-go.com" //very secure I know...


            body.put("client_id", clientID)
            body.put("client_secret", clientSecret)
            body.put("grant_type", "password")
            body.put("username", username)
            body.put("password", password)


            val listener = service.post(url + auth, body)
            listener.setOnResponse { Log.d("RESPONSE RESPONSE RESPONSE", "$it") }
        }
    }

}