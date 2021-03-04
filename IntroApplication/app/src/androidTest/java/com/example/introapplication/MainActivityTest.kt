package com.example.introapplication

import android.provider.AlarmClock.EXTRA_MESSAGE
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


class MainActivityTest {
    @get:Rule var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testEditText() {
        Intents.init()

        val editText = Espresso.onView(ViewMatchers.withId(R.id.main_NameEditText))
        val button = Espresso.onView(ViewMatchers.withId(R.id.main_GreetingButton))

        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val outputStrLength = java.util.Random().nextInt(5) + 10L
        val testString = java.util.Random().ints(outputStrLength, 0, source.length)
            .toArray()
            .map(source::get)
            .joinToString("")

        editText.perform(ViewActions.typeText(testString))
        editText.perform(ViewActions.closeSoftKeyboard())
        button.perform(ViewActions.click())

        intended(hasExtra(EXTRA_MESSAGE, testString))
        Intents.release()
    }
}