package com.github.gogetters.letsgo.testUtil

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

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
    }
}