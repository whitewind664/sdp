package com.github.gogetters.letsgo.activities

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GameActivityTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule<GameActivity>(GameActivity::class.java)

    @Test
    fun tappingScreenPlacesStone() {
        val scenario = activityScenarioRule.scenario
        val goView = Espresso.onView(withParent(withId(R.id.gameBoardFrame)))
        //goView.perform(touchDownAndUp(300F, 300F))
        //goView.perform(touchDownAndUp(500F, 500F))
        //TODO this test will always pass, need to somehow actually test the result...
    }

    private fun touchDownAndUp(x: Float, y: Float): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Send touch events."
            }

            override fun perform(uiController: UiController, view: View) {
                // Get view absolute position
                val location = IntArray(2)
                view.getLocationOnScreen(location)

                // Offset coordinates by view position
                val coordinates = floatArrayOf(x + location[0], y + location[1])
                val precision = floatArrayOf(1f, 1f)

                // Send down event, pause, and send up
                val down = MotionEvents.sendDown(uiController, coordinates, precision).down
                uiController.loopMainThreadForAtLeast(200)
                MotionEvents.sendUp(uiController, down, coordinates)
            }
        }
    }
}