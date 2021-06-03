package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.game.Stone
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GameActivityTest: EmulatedFirebaseTest() {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), GameActivity::class.java)

    init {
        intent.putExtra(GameActivity.EXTRA_GAME_TYPE, "LOCAL")
        intent.putExtra(GameActivity.EXTRA_LOCAL_COLOR, Stone.WHITE.toString())
        intent.putExtra(GameActivity.EXTRA_KOMI, 7.5)
        intent.putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
    }


    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<GameActivity> = ActivityScenarioRule(intent)


    @Test
    fun tappingScreenPlacesStone() {
        val goView = Espresso.onView(withParent(withId(R.id.game_frameLayout_boardFrame)))
        activityScenarioRule.scenario.onActivity {
            goView.perform(touchDownAndUp(1f, 1f))
            goView.perform(touchDownAndUp(2f, 2f))
        }
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