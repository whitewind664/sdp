package com.github.gogetters.letsgo.chat

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.activities.ChatActivity
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MessageAdapterTest: EmulatedFirebaseTest() {
    @get:Rule
    var activityRule = ActivityScenarioRule<ChatActivity>(ChatActivity::class.java)

    @Test
    fun messageIsAdded() {
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            val adapter = MessageAdapter(activity)
            val message = ChatMessage("text", true, Date(), "name")
            adapter.addMessage(message)
            assertEquals(adapter.getItem(0), message)
        }
    }

    @Test
    fun IdStaysTheSame() {
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            val adapter = MessageAdapter(activity)
            val message = ChatMessage("text", true, Date(), "name")
            adapter.addMessage(message)
            val id = adapter.getItemId(0)
            for (i in 1..10)
                adapter.addMessage(message)
            assertEquals(adapter.getItemId(0), id)
        }
    }

    @Test
    fun countIsCorrect() {
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            val adapter = MessageAdapter(activity)
            val message = ChatMessage("text", true, Date(), "name")
            val message2 = ChatMessage("text", false, Date(), "name")
            assertEquals(adapter.count, 0)
            adapter.addMessage(message)
            adapter.addMessage(message)
            adapter.addMessage(message2)
            assertEquals(adapter.count, 3)
        }
    }

}