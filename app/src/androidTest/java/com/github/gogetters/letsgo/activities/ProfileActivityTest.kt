package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.activities.mocking.MockUserBundleProvider
import kotlinx.android.synthetic.main.activity_profile.view.*
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {
    val GRANT_PERMISSION_BUTTON_INDEX = 0
    val PERMISSIONS_DELAY = 5000L

    val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java).putExtra("UserBundleProvider", MockUserBundleProvider())

    lateinit var scenario: ActivityScenario<ProfileActivity>
    
    @Before
    fun init() {
        Intents.init()
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        var waitButton = device.findObject(UiSelector().textContains("wait"))
        if (waitButton.exists()) {
            waitButton.click()
        }

        scenario = ActivityScenario.launch(intent)
        acceptPermissions()
    }

    @After
    fun cleanUp() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun dialogOpensOnProfileClick() {
        onView(withId(R.id.profile_imageView_image)).perform(click())
        onView(withText(R.string.profile_dialogTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun cameraIntentIsFired() {
        onView(withId(R.id.profile_imageView_image)).perform(click())
        //onView(withText(R.string.profile_takePicture)).perform(click())
        clickAtIndex(0, "Take Picture")
        Intents.intended(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    @Test
    fun galleryIntentIsFired() {
        onView(withId(R.id.profile_imageView_image)).perform(click())

        //onView(withText(R.string.profile_chooseFromGallery)).perform(click())
        clickAtIndex(1, "Choose from Gallery")
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_CHOOSER))
    }

    @Test
    fun profilePictureDialogDisappearsOnCancel() {
        onView(withId(R.id.profile_imageView_image)).perform(click())
        //onView(withText(R.string.profile_cancel)).perform(click())
        clickAtIndex(2, "Cancel")
    }

    // @Test
    // Do not test uses firebase
    fun testEmailLogin() {

        val testEmail = ""
        val testPassword = ""

        onView(withText("Sign in with email"))!!.perform(ViewActions.click())

        val emailField = onView(ViewMatchers.withHint("Email"))
        emailField!!.perform(ViewActions.typeText(testEmail), ViewActions.closeSoftKeyboard())

        onView(withText("Next"))!!.perform(ViewActions.click())


        val passwordField = onView(ViewMatchers.withHint("Password"))
        passwordField!!.perform(ViewActions.typeText(testPassword), ViewActions.closeSoftKeyboard())

        onView(withText("Sign in"))!!.perform(ViewActions.click())
    }

    fun acceptPermissions() {
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

    fun clickAtIndex(i: Int, text: String) {
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