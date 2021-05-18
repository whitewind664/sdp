package com.github.gogetters.letsgo.chat.model

import org.junit.Test

import org.junit.Assert.*

class ChatMessageDataTest {

    @Test
    fun toMapContainsCorrectValues() {
        val id: String = "1"
        val text: String ="text"
        val fromId: String = "2"
        val toId: String = "3"
        val sendTime: Long = 3000L

        val data = ChatMessageData(id, text, fromId, toId, sendTime)
        val dataMap = data.toMap()
        assertEquals(id, dataMap["id"])
        assertEquals(text, dataMap["text"])
        assertEquals(fromId, dataMap["fromId"])
        assertEquals(toId, dataMap["toId"])
        assertEquals(sendTime, dataMap["sendTime"])
    }
}