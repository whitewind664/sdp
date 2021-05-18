package com.github.gogetters.letsgo.chat.model

import org.junit.Test

import org.junit.Assert.*

class UserDataTest {

    @Test
    fun toMapContainsCorrectValues() {
        var id: String? = "1"
        var nick: String? = "2"
        var first: String? = "3"
        var last: String? = "4"
        var city: String? = "5"
        var country: String? = "6"

        val data = UserData(id, nick, first, last, city, country)
        val dataMap = data.toMap()
        assertEquals(id, dataMap["id"])
        assertEquals(nick, dataMap["nick"])
        assertEquals(first, dataMap["first"])
        assertEquals(last, dataMap["last"])
        assertEquals(city, dataMap["city"])
        assertEquals(country, dataMap["country"])
    }
}