package com.github.gogetters.letsgo.activities

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseUIActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(FirebaseUIActivity::class.java)

    /**
     * Note that testing this activity is hard as it's directly linked to firebase
     */
    @Test
    fun activityIsCreated() {    }
}