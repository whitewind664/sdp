package com.github.gogetters.letsgo.activities

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.map.mocking.MockLocationSharingService
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiObject
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MapsActivityTest: EmulatedFirebaseTest() {
    val GRANT_PERMISSION_BUTTON_INDEX = 0
    val PERMISSIONS_DELAY = 5000L

    private fun sleep() {
        try {
            Thread.sleep(PERMISSIONS_DELAY)
        } catch (e: InterruptedException) {
            throw RuntimeException("Cannot execute Thread.sleep()")
        }
    }

    @get:Rule var activityRule = ActivityScenarioRule<MapsActivity>(MapsActivity::class.java)

    @Before
    fun init() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        var waitButton = device.findObject(UiSelector().textContains("wait"))
        if (waitButton.exists()) {
            waitButton.click()
        }
        // set locationSharingService
        activityRule.scenario.onActivity { activity ->
            activity.setLocationSharingService(MockLocationSharingService())
        }
    }

    @After
    fun cleanUp() {
        activityRule.scenario.close()
    }

    @Test
    fun otherPlayersAreDisplayedOnButtonClick() {
        onView(withId(R.id.map_button_showPlayers)).perform(click())

        // TODO
    }

    @Test
    fun mapIsDisplayedIfPermissionIsGranted() {
        val scenario = activityRule.scenario
        getInstrumentation()

        // in case the permission hasn't been requested
        val device: UiDevice = UiDevice.getInstance(getInstrumentation())
        val allowPermissionsButton: UiObject = device.findObject(UiSelector()
                .clickable(true)
                .checkable(false)
                .index(GRANT_PERMISSION_BUTTON_INDEX))
        if (allowPermissionsButton.exists()) {
            allowPermissionsButton.click();
        }

        // after waiting for a while, check whether the map is displayed
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

}