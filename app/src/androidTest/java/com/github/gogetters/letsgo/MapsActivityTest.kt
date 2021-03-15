package com.github.gogetters.letsgo

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MapsActivityTest {
    @get:Rule var activityScenarioRule = ActivityScenarioRule<MapsActivity>(MapsActivity::class.java)

    @Test
    fun testMap() {
        // TODO actually test something
        val scenario = activityScenarioRule.scenario
    }

}