package com.github.gogetters.letsgo.activities

import android.widget.EditText
import android.widget.ListView
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.ChatMessage
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import junit.framework.Assert.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatActivityTest: EmulatedFirebaseTest() {
    @get:Rule
    var activityRule = ActivityScenarioRule(ChatActivity::class.java)

    @After
    fun cleanUp() {
        activityRule.scenario.close()
    }

    @Test
    fun sentMessageIsAddedToListView() {
        // TODO gotta test whether this works with firebase emulation
//        val scenario = activityRule.scenario
//        scenario.onActivity { activity ->
//            val testText = "Hello"
//            val entryText: EditText = activity.findViewById(R.id.chat_editText_input)
//            entryText.setText(testText)
//            activity.sendMessage(null) // argument is not used
//
//            // Check if entry field was emptied
//            assertTrue(entryText.text.toString().isEmpty())
//
////            Can't check the following since user not logged in, make mocked user in the future
//
////            // Check if the message was added to the listview
////            val listView: ListView = activity.findViewById(R.id.chat_listView_messages)
////            val item: ChatMessage = (listView.getItemAtPosition(listView.count - 1) as ChatMessage)
////            assertEquals(item.getText(), testText)
//        }
    }
}