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
        assertEquals(data.id, dataMap["id"])
        assertEquals(data.text, dataMap["text"])
        assertEquals(data.fromId, dataMap["fromId"])
        assertEquals(data.toId, dataMap["toId"])
        assertEquals(data.sendTime, dataMap["sendTime"])
    }
}