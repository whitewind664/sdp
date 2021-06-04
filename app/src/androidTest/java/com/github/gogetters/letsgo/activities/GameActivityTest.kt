package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.game.Player
import com.github.gogetters.letsgo.testUtil.TestUtils
import com.github.gogetters.letsgo.testUtil.ToastMatcher
import junit.framework.Assert.assertTrue
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GameActivityTest: EmulatedFirebaseTest() {

    val intent = Intent(ApplicationProvider.getApplicationContext(), GameActivity::class.java).apply {
        putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
        putExtra(GameActivity.EXTRA_KOMI, 5.5)
        val localType = Player.PlayerTypes.LOCAL.ordinal
        putExtra(GameActivity.EXTRA_PLAYER_BLACK, localType)
        putExtra(GameActivity.EXTRA_PLAYER_WHITE, localType)
    }
    lateinit var scenario: ActivityScenario<GameActivity>

    @Before
    fun init() {
        scenario = ActivityScenario.launch(intent)
        TestUtils.clickWaitButton()
    }

    @After
    fun cleanUp() {
        scenario.close()
    }

    //@Test
    fun tappingScreenPlacesStone() {
        scenario = ActivityScenario.launch(intent)
        val goView = onView(withParent(withId(R.id.game_frameLayout_boardFrame)))
        goView.perform(touchDownAndUp(1f, 1f))
        goView.perform(touchDownAndUp(2f, 2f))
    }

    @Test
    fun btLocalPlayerBlackTriggersToast() {
        val btIntent = Intent(ApplicationProvider.getApplicationContext(), GameActivity::class.java).apply {
            putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
            putExtra(GameActivity.EXTRA_KOMI, 5.5)
            putExtra(GameActivity.EXTRA_PLAYER_BLACK, Player.PlayerTypes.BTLOCAL.ordinal)
            putExtra(GameActivity.EXTRA_PLAYER_WHITE, Player.PlayerTypes.BTREMOTE.ordinal)
        }

        scenario = ActivityScenario.launch(btIntent)
        onView(withText(R.string.game_startAsBlack)).inRoot(ToastMatcher()).check(matches((isDisplayed())))
    }

    @Test
    fun btLocalPlayerWhiteTriggersToast() {
        val btIntent = Intent(ApplicationProvider.getApplicationContext(), GameActivity::class.java).apply {
            putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
            putExtra(GameActivity.EXTRA_KOMI, 5.5)
            putExtra(GameActivity.EXTRA_PLAYER_WHITE, Player.PlayerTypes.BTLOCAL.ordinal)
            putExtra(GameActivity.EXTRA_PLAYER_BLACK, Player.PlayerTypes.BTREMOTE.ordinal)
        }

        scenario = ActivityScenario.launch(btIntent)
        onView(withText(R.string.game_startAsWhite)).inRoot(ToastMatcher()).check(matches((isDisplayed())))
    }

    @Test
    fun passCanBeCanceled() {
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.game_button_pass)).perform(click())
        onView(withText(R.string.game_passTitle))
            .check(matches(isDisplayed()))
        clickAtIndex(1, "Cancel")
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val blackNextText = device.findObject(UiSelector().textContains("BLACK plays"))
        assertTrue(blackNextText.exists()) // still black's turn
    }

    @Test
    fun twoSubsequentPassesEndGame() {
        scenario = ActivityScenario.launch(intent)
        TestUtils.sleep()
        TestUtils.sleep()
        onView(withId(R.id.game_button_pass)).perform(click())
        onView(withText(R.string.game_passTitle))
            .check(matches(isDisplayed()))
        clickAtIndex(0, "Confirm PASS")

        onView(withId(R.id.game_button_pass)).perform(click())
        onView(withText(R.string.game_passTitle))
            .check(matches(isDisplayed()))
        clickAtIndex(0, "Confirm PASS")

        // verify that end of game is displayed
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val endOfGameText = device.findObject(UiSelector().textContains("Game is over"))
        assertTrue(endOfGameText.exists())
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

    private fun clickAtIndex(i: Int, text: String) {
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val button: UiObject = device.findObject(
            UiSelector()
                .clickable(true)
                .checkable(false)
                .index(i)
                .text(text))
        assertTrue(button.exists())
        button.click()
    }
}