package com.github.gogetters.letsgo.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.map.mocking.MockLocationSharingService
import com.google.android.gms.maps.model.LatLng
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MapsActivityTest : EmulatedFirebaseTest() {
    val GRANT_PERMISSION_BUTTON_INDEX = 0
    val PERMISSIONS_DELAY = 5000L

    val userPath = "users/"
    val isActivePath = "/isLookingForPlayers"
    val latPath = "/lastPositionLatitude"
    val lngPath = "/lastPositionLongitude"
    val EPFL: LatLng = LatLng(46.51899505106699, 6.563449219980816)

    private fun sleep() {
        try {
            Thread.sleep(PERMISSIONS_DELAY)
        } catch (e: InterruptedException) {
            throw RuntimeException("Cannot execute Thread.sleep()")
        }
    }

    @get:Rule
    var activityRule = ActivityScenarioRule<MapsActivity>(MapsActivity::class.java)

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
    fun messageIsDisplayedWhenNoPlayersFound() {

    }

    @Test
    fun otherPlayersAreDisplayedOnButtonClick() {
        // add a dummy player to database
        val testId = "mapTestId"
        Database.writeData("$userPath/$testId$isActivePath$testId", true)
        Database.writeData("$userPath/$testId$lngPath", EPFL.longitude)
        Database.writeData("$userPath/$testId$latPath", EPFL.latitude)

        // test that this is retrieved
        onView(withId(R.id.map_button_showPlayers)).perform(click())
        val device = UiDevice.getInstance(getInstrumentation())
        val marker = device.findObject(UiSelector().descriptionContains(testId))
        assertTrue(marker.isClickable)
    }

    @Test
    fun otherPlayersAreUpdatedOnSecondClick() {

    }

    @Test
    fun mapIsDisplayedIfPermissionIsGranted() {
        val scenario = activityRule.scenario
        getInstrumentation()

        // in case the permission hasn't been requested
        val device: UiDevice = UiDevice.getInstance(getInstrumentation())
        val allowPermissionsButton: UiObject = device.findObject(
            UiSelector()
                .clickable(true)
                .checkable(false)
                .index(GRANT_PERMISSION_BUTTON_INDEX)
        )
        if (allowPermissionsButton.exists()) {
            allowPermissionsButton.click();
        }

        // after waiting for a while, check whether the map is displayed
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

}