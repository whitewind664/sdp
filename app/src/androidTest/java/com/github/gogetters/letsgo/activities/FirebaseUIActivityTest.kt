package com.github.gogetters.letsgo.activities

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseUIActivityTest: EmulatedFirebaseTest() {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(FirebaseUIActivity::class.java)

    /**
     * Note that testing this activity is hard as it's directly linked to firebase
     */
    @Test
    fun activityIsCreated() {    }
}