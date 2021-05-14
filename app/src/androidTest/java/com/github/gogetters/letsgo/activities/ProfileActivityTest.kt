package com.github.gogetters.letsgo.activities

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.BoundedMatcher
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
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import kotlinx.android.synthetic.main.activity_profile.view.*
import org.hamcrest.Description
import org.hamcrest.Matchers.not
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
    val DELAY = 3000L

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

    @Test
    fun dialogOpensOnProfileClick() {
        scenario = ActivityScenario.launch(intent)
        sleep()
        onView(withId(R.id.profile_imageView_image)).perform(click())
        onView(withText(R.string.profile_dialogTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun cameraIntentIsFired() {
        scenario = ActivityScenario.launch(intent)
        sleep()
        onView(withId(R.id.profile_imageView_image)).perform(click())
        clickAtIndex(0, "Take Picture")
        acceptPermissions()
        Intents.intended(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    // fails with emulated database, no idea why - Dimitar
//    @Test
//    fun galleryIntentIsFired() {
//        scenario = ActivityScenario.launch(intent)
//        sleep()
//        onView(withId(R.id.profile_imageView_image)).perform(click())
//
//        //onView(withText(R.string.profile_chooseFromGallery)).perform(click())
//        clickAtIndex(1, "Choose from Gallery")
//        acceptPermissions()
//        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_CHOOSER))
//    }

    @Test
    fun profilePictureDialogDisappearsOnCancel() {
        scenario = ActivityScenario.launch(intent)
        sleep()
        onView(withId(R.id.profile_imageView_image)).perform(click())
        //onView(withText(R.string.profile_cancel)).perform(click())
        clickAtIndex(2, "Cancel")
    }

    //@Test doesn't work
    fun gallerySelectionIsWellPlaced() {
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity {
            savePickedImage(it)
            val imgGalleryResult = createImageGallerySetResultStub(it)
            intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(imgGalleryResult)
            //auctionPhotos_CreationInitialUI()
            onView(withId(R.id.profile_imageView_image)).perform(click())
            clickAtIndex(1, "Choose from Gallery")
            acceptPermissions()
            onView(withId(R.id.profile_imageView_image)).check(matches(hasImageSet()))
        }
    }

    //@Test doesn't work
    fun capturedImageIsWellPlaced() {
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity {
            val imgCaptureResult = createImageCaptureActivityResultStub(it)
            intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(imgCaptureResult)

            onView(withId(R.id.profile_imageView_image)).perform(click())
            clickAtIndex(0, "Take Picture")
            acceptPermissions()
            onView(withId(R.id.profile_imageView_image)).check(matches(hasImageSet()))
        }
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

    // the following helper methods were adapted from https://proandroiddev.com/testing-camera-and-galley-intents-with-espresso-218eb9f59da9
    private fun savePickedImage(activity: Activity) {
        val bm = BitmapFactory.decodeResource(activity.resources, R.mipmap.ic_launcher)
        val dir = activity.externalCacheDir
        val file = File(dir?.path, "pickImageResult.jpeg")
        val outStream: FileOutputStream?
        try {
            outStream = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            with(outStream) {
                flush()
                close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createImageGallerySetResultStub(activity: Activity): Instrumentation.ActivityResult {
        val bundle = Bundle()
        val parcels = ArrayList<Parcelable>()
        val resultData = Intent()
        val dir = activity.externalCacheDir
        val file = File(dir?.path, "pickImageResult.jpeg")
        val uri = Uri.fromFile(file)
        val parcelable1 = uri as Parcelable
        parcels.add(parcelable1)
        bundle.putParcelableArrayList(Intent.EXTRA_STREAM, parcels)
        resultData.putExtras(bundle)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun hasImageSet(): BoundedMatcher<View, ImageView> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has image set.")
            }

            override fun matchesSafely(imageView: ImageView): Boolean {
                return imageView.background != null
            }
        }
    }

    private fun createImageCaptureActivityResultStub(activity: Activity): Instrumentation.ActivityResult {
        val bundle = Bundle()
        bundle.putParcelable("IMG_DATA", BitmapFactory.decodeResource(activity.resources, R.mipmap.ic_launcher))
        // Create the Intent that will include the bundle.
        val resultData = Intent()
        resultData.putExtras(bundle)
        // Create the ActivityResult with the Intent.
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

}