package com.github.gogetters.letsgo.chat.model

/**
 * The ChatMessageData class represents the chats in the database
 */
data class ChatMessageData(
    val id: String,
    val text: String,
    val fromId: String,
    val toId: String,
    val sendTime: Long
) {

    /**
     * Empty constructor is required to parse the object
     */
    constructor() : this("", "", "", "", -1)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "text" to text,
            "fromId" to fromId,
            "toId" to toId,
            "sendTime" to sendTime
        )
    }

}