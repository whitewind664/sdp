package com.github.gogetters.letsgo.matchmaking;

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.activities.MainActivity
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.testUtil.TestUtils
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class OnlinePlayTest: EmulatedFirebaseTest() {
    init {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        FirebaseApp.initializeApp(appContext)
    }

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @After
    fun cleanUp() {
//        activityRule.scenario.close()
        activityRule.scenario.moveToState(Lifecycle.State.DESTROYED)
        Authentication.signOut()
    }

    @Test
    fun queueRankedAndLeave() {
        Authentication.signOut()
        Authentication.anonymousSignIn()
        onView(withId(R.id.main_button_play)).perform(click())
        onView(withId(R.id.gameModeChooser_button_ranked)).perform(click())
        var x = Tasks.await(Database.readData("/matchmaking/currentlyWaiting/ranked")).value
        assertNotEquals(null, x)

        var canGo = false

        Database.findMatch("fakeId", 1500, true, {_,_,_ ->
            canGo = true
        })

        while (!canGo) {}

        Tasks.await(Database.writeData("/matchmaking/users/fakeId/rating", 1500))
        x = Tasks.await(Database.readData("/matchmaking/currentlyWaiting/ranked")).value
        assertEquals(null, x)

//        Matchmaking.surrender()
//        TestUtils.sleep()
//        x = Tasks.await(Database.readData("/matchmaking/users/${Authentication.getUid()}/rating")).getValue(Int::class.java)
//        assertNotEquals(1500, x)
    }
}
