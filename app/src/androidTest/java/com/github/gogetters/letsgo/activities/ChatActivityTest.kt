package com.github.gogetters.letsgo.activities

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.github.gogetters.letsgo.database.user.LetsGoUser
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatActivityTest: EmulatedFirebaseTest() {

    val ITEM_IN_TEST = 0
    val USER_IN_TEST = LetsGoUser("id")

    @get:Rule
    var activityRule = ActivityScenarioRule(ChatActivity::class.java)

    @Before
    fun init() {
        Intents.init()
        USER_IN_TEST.nick = "nick"
        USER_IN_TEST.first = "first"
        USER_IN_TEST.last = "last"
        USER_IN_TEST.city = "city"
        USER_IN_TEST.country = "country"
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityRule.scenario.close()
    }

    @Ignore
    @Test
    fun test_isItemsVisible_onAppLaunch() {
        onView(withId(R.id.chat_recyclerview_messages)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        onView(withId(R.id.chat_editText_input)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.chat_send_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Ignore
    @Test
    fun test_sendMessagesIsAddedToListView() {
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            val testText = "Hello"
            val entryText: EditText = activity.findViewById(R.id.chat_editText_input)
            entryText.setText(testText)
            activity.sendMessage() // argument is not used

            // Check if entry field was emptied
            //assertTrue(entryText.text.toString().isEmpty())

//          Can't check the following since user not logged in, make mocked user in the future

//          // Check if the message was added to the listview
//          val listView: ListView = activity.findViewById(R.id.chat_listView_messages)
//          val item: ChatMessageData = (listView.getItemAtPosition(listView.count - 1) as ChatMessageData)
//          assertEquals(item.getText(), testText)
        }
    }

}
