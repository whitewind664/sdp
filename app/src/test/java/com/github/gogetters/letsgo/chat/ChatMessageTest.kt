package com.github.gogetters.letsgo.chat

import com.github.gogetters.letsgo.chat.model.ChatMessageData
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*


class ChatMessageTest {

    @Test
    fun messageGettersWork() {
        val text = "message"
        val belongsToUser = true
        val time = Date()
        val userName = "username"
        /*val message = ChatMessageData(text, belongsToUser, time, userName)
        assertEquals(message.getText(), text)
        assertEquals(message.isBelongingToUser(), belongsToUser)
        assertEquals(message.getSendTime(), time)
        assertEquals(message.getUserName(), userName)

         */
    }
}