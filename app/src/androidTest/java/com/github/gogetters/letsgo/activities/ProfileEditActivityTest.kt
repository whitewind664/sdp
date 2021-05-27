package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.activities.mocking.MockUserBundleProvider
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileEditActivityTest : EmulatedFirebaseTest() {
    val DELAY = 5000L

    val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileEditActivity::class.java).putExtra("UserBundleProvider", MockUserBundleProvider())
    lateinit var scenario: ActivityScenario<ProfileEditActivity>

    private fun sleep() {
        try {
            Thread.sleep(DELAY)
        } catch (e: InterruptedException) {
            throw RuntimeException("Cannot execute Thread.sleep()")
        }
    }

    private fun clickWaitButton() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        var waitButton = device.findObject(UiSelector().textContains("wait"))
        if (waitButton.exists()) {
            waitButton.click()
        }
    }

    @Before
    fun init() {
        Intents.init()
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun cleanUp() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun testProfileEditForMockUser() {
        scenario = ActivityScenario.launch(intent)
        Espresso.onView(ViewMatchers.withId(R.id.profile_edit_button_save)).perform(ViewActions.click())
    }
}