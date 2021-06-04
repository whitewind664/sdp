package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.database.user.FirebaseUserBundle
import com.github.gogetters.letsgo.testUtil.TestUtils.Companion.clickWaitButton
import com.github.gogetters.letsgo.testUtil.TestUtils.Companion.sleep
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest : EmulatedFirebaseTest() {

    val intent = Intent(ApplicationProvider.getApplicationContext(), LoginActivity::class.java)
    lateinit var scenario: ActivityScenario<LoginActivity>

    @Before
    fun init() {
        Intents.init()
        Authentication.signOut()
        scenario = ActivityScenario.launch(intent)
        clickWaitButton()
    }

    @After
    fun cleanUp() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun returnsToProfileActivityAfterRegister() {
        val testEmail = "TODELETE@letsgo.com"
        val testName = "name"
        val testPwd = "VJDKFjdkfjisljfie233"
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val loginButton: UiObject = device.findObject(
            UiSelector()
                .clickable(true)
                .checkable(false)
                .textContains("Sign in")
        )
        if (loginButton.exists()) {
            loginButton.click()
        }

        val emailText = device.findObject(UiSelector().className("android.widget.EditText"))
        emailText.text = testEmail

        // click next button
        val nextButton = device.findObject(UiSelector().clickable(true).textContains("NEXT"))
        if (nextButton.exists()) {
            nextButton.click()
        }

        val nameText =
            device.findObject(UiSelector().className("android.widget.EditText").instance(1))
        val pwdText =
            device.findObject(UiSelector().className("android.widget.EditText").instance(2))
        nameText.text = testName
        pwdText.text = testPwd

        val saveButton = device.findObject(UiSelector().clickable(true).textContains("SAVE"))
        if (saveButton.exists()) {
            saveButton.click()
        }

        /*val savePasswordButton = device.findObject(UiSelector().clickable(true).textContains("Save"))
        if (savePasswordButton.exists()) {
            savePasswordButton.click()
        }*/

        sleep()
        Authentication.getCurrentUser()!!.delete()
        /*val user = Authentication.getCurrentUser()
        if (user != null) {
            val userBundle = FirebaseUserBundle(user)
            userBundle.deleteUser()
        } else {
            throw IllegalStateException()
        } */

        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
    }
}