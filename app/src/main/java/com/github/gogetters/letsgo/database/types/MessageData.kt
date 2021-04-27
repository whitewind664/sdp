package com.github.gogetters.letsgo.database.types

import java.util.*

data class MessageData(val messageText: String = "", val senderId: String = "", val timestamp: Long = 0) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "messageText" to messageText,
            "senderId" to senderId,
            "timestamp" to timestamp
        )
    }
}