package com.github.gogetters.letsgo.testUtil

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.ExecutionException

class TestUtils {
    companion object {
        val DELAY = 5000L

        fun sleep() {
            try {
                Thread.sleep(DELAY)
            } catch (e: InterruptedException) {
                throw RuntimeException("Cannot execute Thread.sleep()")
            }
        }

        fun clickWaitButton() {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            var waitButton = device.findObject(UiSelector().textContains("wait"))
            if (waitButton.exists()) {
                waitButton.click()
            }
        }

        fun makeSureTestUserAuthenticated() {
            if (Firebase.auth.currentUser == null) {
                val testEmail = "testerAuth@test.com"
                val testPass = "123456"
                try {
                    Tasks.await(Firebase.auth.createUserWithEmailAndPassword(testEmail, testPass))
                } catch (e: ExecutionException) {
                    Firebase.auth.signInWithEmailAndPassword(testEmail, testPass)
                }
            }
        }
    }
}