package com.github.gogetters.letsgo.activities

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TutorialActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(TutorialActivity::class.java)

    @Test
    fun boardExplanationIsShowedSecond() {

    }

    @Test
    fun buttonTextChangesAtEndOfTutorial() {

    }
}