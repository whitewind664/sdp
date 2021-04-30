package com.github.gogetters.letsgo.database

import com.github.gogetters.letsgo.database.types.*
import org.junit.Test
import org.junit.Assert.assertEquals

class TypeTests {
    @Test
    fun chatDataMap() {
        val text = "testing123"
        val chatData = ChatData(text)
        val map = chatData.toMap()
        assertEquals(text, map["lastMessageText"])
    }

    @Test
    fun gameDataMap() {
        val player1 = "player1"
        val player2 = "player2"
        val gameData = GameData(player1, player2)
        val map = gameData.toMap()
        assertEquals(player1, map["player1"])
        assertEquals(player2, map["player2"])
    }

    @Test
    fun messageDataMap() {
        val messageText = "text123"
        val messageData = MessageData(messageText)
        val map = messageData.toMap()
        assertEquals(messageText, map["messageText"])
    }
}