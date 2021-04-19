package com.github.gogetters.letsgo.activities

import android.widget.EditText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import junit.framework.Assert
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
        scenario.onActivity { activity ->
            
        }
    }
}