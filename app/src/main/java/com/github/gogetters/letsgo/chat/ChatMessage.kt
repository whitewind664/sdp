package com.github.gogetters.letsgo.chat

import java.util.*

class ChatMessage(private val text: String, private val belongsToUser: Boolean, private val sendTime: Date, private val userName: String) {
    
    fun getText(): String {
        return text
    }

    fun isBelongingToUser(): Boolean {
        return belongsToUser
    }

    fun getSendTime(): Date {
        return sendTime
    }

    fun getUserName(): String {
        return userName
    }
}