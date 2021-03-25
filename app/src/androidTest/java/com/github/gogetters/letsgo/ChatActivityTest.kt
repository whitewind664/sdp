package com.github.gogetters.letsgo

import android.widget.EditText
import android.widget.ListView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.chat.ChatMessage
import junit.framework.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule<ChatActivity>(ChatActivity::class.java)

    @Test
    fun sentMessageIsAddedToListView() {
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            val testText = "Hello"
            val entryText: EditText = activity.findViewById(R.id.editText)
            entryText.setText(testText)
            activity.sendMessage(null) // argument is not used

            // Check if entry field was emptied
            assertTrue(entryText.text.toString().isEmpty())
            // Check if the message was added to the listview
            val listView: ListView = activity.findViewById(R.id.messages_view)
            val item: ChatMessage = (listView.getItemAtPosition(0) as ChatMessage)
            assertEquals(item.getText(), testText)
        }
    }
}