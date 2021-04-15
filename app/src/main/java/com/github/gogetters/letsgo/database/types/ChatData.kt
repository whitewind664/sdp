package com.github.gogetters.letsgo.database.types

data class ChatData(val lastMessageText: String = "") {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "lastMessageText" to lastMessageText
        )
    }
}