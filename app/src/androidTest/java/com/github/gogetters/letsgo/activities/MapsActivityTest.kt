package com.github.gogetters.letsgo.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.testUtil.TestUtils
import com.github.gogetters.letsgo.testUtil.TestUtils.Companion.sleep
import com.google.android.gms.maps.model.LatLng
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MapsActivityTest : EmulatedFirebaseTest() {
    val GRANT_PERMISSION_BUTTON_INDEX = 0
    val PERMISSIONS_DELAY = 5000L
    private val PACKAGE_NAME = "com.github.gogetters.letsgo"

    val userPath = "users"
    val isActivePath = "/isLookingForPlayers"
    val latPath = "/lastPositionLatitude"
    val lngPath = "/lastPositionLongitude"
    val EPFL: LatLng = LatLng(46.51899505106699, 6.563449219980816)

    @get:Rule
    val exception: ExpectedException = ExpectedException.none()


    @get:Rule
    var activityRule = ActivityScenarioRule(MapsActivity::class.java)

    @Before
    fun init() {
        TestUtils.clickWaitButton()
    }

    @After
    fun cleanUp() {
        activityRule.scenario.close()
    }

    @Test
    fun noMarkerDisplayedWhenNoPlayersFound() {
        exception.expect(UiObjectNotFoundException::class.java)
        onView(withId(R.id.map_button_showPlayers)).perform(click())
        val device = UiDevice.getInstance(getInstrumentation())
        val marker = device.findObject(UiSelector().descriptionContains("user"))
        marker.click()
    }

    @Test // runs locally but not on Cirrus... :((
    fun otherPlayersAreDisplayedOnButtonClick() {
        val device = UiDevice.getInstance(getInstrumentation())

        // add a dummy player to database
        val testId = "mapTestId"
        Database.writeData("$userPath/$testId$isActivePath", true)
        Database.writeData("$userPath/$testId$lngPath", EPFL.longitude)
        Database.writeData("$userPath/$testId$latPath", EPFL.latitude)

        // test that this is retrieved
        val button = device.findObject(By.res(PACKAGE_NAME, "map_button_showPlayers"))
        button.click()
        sleep()

        val marker = device.findObject(UiSelector().descriptionContains(testId))
        assertTrue(marker.exists()) // somehow doesn't pass on cirrus
        marker.click() // would throw an exception if it was not displayed

        Database.deleteData("$userPath/$testId")
    }

    @Test // runs locally but not on Cirrus... :((
    fun otherPlayersAreUpdatedOnSecondClick() {
        val device = UiDevice.getInstance(getInstrumentation())
        val testId = "mapTestId1"
        Database.writeData("$userPath/$testId$isActivePath", true)
        Database.writeData("$userPath/$testId$lngPath", EPFL.longitude)
        Database.writeData("$userPath/$testId$latPath", EPFL.latitude)

        // First demand
        val button = device.findObject(By.res(PACKAGE_NAME, "map_button_showPlayers"))
        button.click()

        // replace user
        sleep()
        Database.deleteData("$userPath/$testId")
        val testId2 = "mapTestId2"
        Database.writeData("$userPath/$testId2$isActivePath", true)
        Database.writeData("$userPath/$testId2$lngPath", EPFL.longitude + 1)
        Database.writeData("$userPath/$testId2$latPath", EPFL.latitude + 1)

        val button2 = device.findObject(By.res(PACKAGE_NAME, "map_button_showPlayers"))
        button2.click()
        sleep()
        // check if everything was updated
        val marker1 = device.findObject(UiSelector().descriptionContains(testId))
        assertFalse(marker1.exists())
        val marker2 = device.findObject(UiSelector().descriptionContains(testId2))
        assertTrue(marker2.exists())
        marker2.click() // would throw an exception if it was not displayed

        Database.deleteData("$userPath/$testId2")
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