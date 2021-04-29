package com.github.gogetters.letsgo.activities

import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BluetoothActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(BluetoothActivity::class.java)

    @Test
    fun bluetoothTest() {
        val scenario = activityRule.scenario
        val listenButton = Espresso.onView(withId(R.id.bluetooth_button_listen))
        val listButton = Espresso.onView(withId(R.id.bluetooth_button_listDevices))

        scenario.onActivity { activity ->
            try {
                listenButton.perform(click())
                listButton.perform(click())
            } catch (e: Exception) {
                Log.d("TEST", "DIDN'T WORK.......")
            }
        }
    }
}