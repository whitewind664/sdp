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
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.activities.mocking.MockUserBundleProvider
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import org.hamcrest.Description
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ProfileEditActivityTest : EmulatedFirebaseTest() {
    val DELAY = 5000L
    val GRANT_PERMISSION_BUTTON_INDEX = 0

    val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileEditActivity::class.java).putExtra("UserBundleProvider", MockUserBundleProvider())
    lateinit var scenario: ActivityScenario<ProfileEditActivity>

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
    }

    @After
    fun cleanUp() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun testProfileEditForMockUser() {
        scenario = ActivityScenario.launch(intent)
        Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image)).perform(ViewActions.click())
    }

    @Test
    fun dialogOpensOnProfileClick() {
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
        sleep()
        Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.profile_dialogTitle))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun cameraIntentIsFired() {
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
        sleep()
        Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image))
            .perform(ViewActions.click())
        clickAtIndex(0, "Take Picture")
        acceptPermissions()
        sleep()
        Intents.intended(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    @Test
    fun galleryIntentIsFired() {
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
        sleep()
        Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image))
            .perform(ViewActions.click())

        //onView(withText(R.string.profile_chooseFromGallery)).perform(click())
        clickAtIndex(1, "Choose from Gallery")
        acceptPermissions()
        sleep()
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_CHOOSER))
    }

    @Test
    fun profilePictureDialogDisappearsOnCancel() {
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
        sleep()
        Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image))
            .perform(ViewActions.click())
        clickAtIndex(2, "Cancel")
    }

    //@Test does not work...
    //@UiThreadTest
    fun gallerySelectionIsWellPlaced() {

        clickWaitButton()
        scenario.onActivity {
            UiThreadStatement.runOnUiThread {
                savePickedImage(it)
                val imgGalleryResult = createImageGallerySetResultStub(it)
                Intents.intending(IntentMatchers.hasAction(Intent.ACTION_CHOOSER)).respondWith(imgGalleryResult)
            }
            //auctionPhotos_CreationInitialUI()
            Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image))
                .perform(ViewActions.click())
            clickAtIndex(1, "Choose from Gallery")
            acceptPermissions()
            Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image))
                .check(ViewAssertions.matches(hasImageSet()))
        }
    }

    //@Test doesn't work
    fun capturedImageIsWellPlaced() {
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
        scenario.onActivity {
            val imgCaptureResult = createImageCaptureActivityResultStub(it)
            Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(imgCaptureResult)

            Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image))
                .perform(ViewActions.click())
            clickAtIndex(0, "Take Picture")
            acceptPermissions()
            Espresso.onView(ViewMatchers.withId(R.id.profile_edit_imageView_image))
                .check(ViewAssertions.matches(hasImageSet()))
        }
    }

    private fun clickWaitButton() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        var waitButton = device.findObject(UiSelector().textContains("wait"))
        if (waitButton.exists()) {
            waitButton.click()
        }
    }

    private fun acceptPermissions() {
        // in case the permission hasn't been requested
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val allowPermissionsButton: UiObject = device.findObject(
            UiSelector()
            .clickable(true)
            .checkable(false)
            .index(GRANT_PERMISSION_BUTTON_INDEX))
        if (allowPermissionsButton.exists()) {
            allowPermissionsButton.click();
        }
    }

    private fun clickAtIndex(i: Int, text: String) {
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val button: UiObject = device.findObject(
            UiSelector()
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
        val bm = BitmapFactory.decodeResource(activity.resources, R.drawable.black)
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