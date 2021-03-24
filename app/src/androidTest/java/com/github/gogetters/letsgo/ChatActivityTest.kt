package com.github.gogetters.letsgo

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule<ChatActivity>(ChatActivity::class.java)

    @Test
    fun sentMessageIsDisplayed() {
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            // TODO implement
        }
    }
}