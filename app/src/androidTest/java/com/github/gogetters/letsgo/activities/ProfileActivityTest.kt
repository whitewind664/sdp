package com.github.gogetters.letsgo.activities

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.activities.mocking.MockUserBundleProvider
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import kotlinx.android.synthetic.main.activity_profile.view.*
import org.hamcrest.Description
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ProfileActivityTest: EmulatedFirebaseTest() {
    val GRANT_PERMISSION_BUTTON_INDEX = 0
    val DELAY = 5000L

    val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java).putExtra("UserBundleProvider", MockUserBundleProvider())

    lateinit var scenario: ActivityScenario<ProfileActivity>

    private fun sleep() {
        try {
            Thread.sleep(DELAY)
        } catch (e: InterruptedException) {
            throw RuntimeException("Cannot execute Thread.sleep()")
        }
    }

    @Before
    fun init() {
        Intents.init()
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
    }

    private fun clickWaitButton() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        var waitButton = device.findObject(UiSelector().textContains("wait"))
        if (waitButton.exists()) {
            waitButton.click()
        }
    }

    @After
    fun cleanUp() {
        Intents.release()
        scenario.close()
    }



    // @Test
    // Do not test uses firebase
    fun testEmailLogin() {

        val testEmail = ""
        val testPassword = ""

        onView(withText("Sign in with email"))!!.perform(click())

        val emailField = onView(withHint("Email"))
        emailField!!.perform(ViewActions.typeText(testEmail), ViewActions.closeSoftKeyboard())

        onView(withText("Next"))!!.perform(click())


        val passwordField = onView(withHint("Password"))
        passwordField!!.perform(ViewActions.typeText(testPassword), ViewActions.closeSoftKeyboard())

        onView(withText("Sign in"))!!.perform(click())
    }

    private fun acceptPermissions() {
        // in case the permission hasn't been requested
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val allowPermissionsButton: UiObject = device.findObject(UiSelector()
                .clickable(true)
                .checkable(false)
                .index(GRANT_PERMISSION_BUTTON_INDEX))
        if (allowPermissionsButton.exists()) {
            allowPermissionsButton.click();
        }
    }

    private fun clickAtIndex(i: Int, text: String) {
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val button: UiObject = device.findObject(UiSelector()
                .clickable(true)
                .checkable(false)
                .index(i)
                .text(text))
        if (button.exists()) {
            button.click();
        }
    }

}