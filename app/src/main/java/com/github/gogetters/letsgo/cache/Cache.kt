package com.github.gogetters.letsgo.cache

import android.content.SharedPreferences
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class Cache {

    companion object {

        //////////////////////////////////////////////////////////
        // SharedPreferences keys to store data
        //////////////////////////////////////////////////////////
        const val PREF_ID = "sharedPrefs"
        // Keys to store user related info
        const val USER_UID = "userUid"
        const val USER_NICK = "userNick"
        const val USER_FIRST = "userFirst"
        const val USER_LAST = "userLast"
        const val USER_COUNTRY = "userCountry"
        const val USER_CITY = "userCity"

        //////////////////////////////////////////////////////////
        // User info local storage
        //////////////////////////////////////////////////////////
        /**
         * Enables saving user info in local cache for the profile activity UI
         */
        fun saveUserProfile(sharedPreferences: SharedPreferences?, user: LetsGoUser?) {

            if (sharedPreferences == null || user == null) return

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.apply {

                putString(USER_UID, user.uid)
                putString(USER_NICK, user.nick)
                putString(USER_FIRST, user.first)
                putString(USER_LAST, user.last)
                putString(USER_COUNTRY, user.country)
                putString(USER_CITY, user.city)

            }.apply()
        }

        /**
         * Enables loading user info from local cache for the profile activity UI
         */
        fun loadUserProfile(sharedPreferences: SharedPreferences?): LetsGoUser? {

            if (sharedPreferences == null) return null

            val user = LetsGoUser(USER_UID)
            user.nick = sharedPreferences.getString(USER_NICK, null)
            user.first = sharedPreferences.getString(USER_FIRST, null)
            user.last = sharedPreferences.getString(USER_LAST, null)
            user.country = sharedPreferences.getString(USER_COUNTRY, null)
            user.city = sharedPreferences.getString(USER_CITY, null)

            return user
        }

        //////////////////////////////////////////////////////////
        // Chat data local storage
        //////////////////////////////////////////////////////////
        /**
         * Enables saving chat messages list in local cache for the ChatActivity
         */
        fun saveChatData(sharedPreferences: SharedPreferences?, key: String?, items: ArrayList<ChatMessageData>?) {

            if (sharedPreferences == null || key == null || items == null) return

            val editor = sharedPreferences.edit()
            val json = Gson().toJson(items)
            editor.apply {

                putString(key, json)

            }.apply()
        }

        /**
         * Enables loading chat messages in local cache for the ChatActivity
         */
        fun loadChatData(sharedPreferences: SharedPreferences?, key: String?): ArrayList<ChatMessageData> {

            if (sharedPreferences == null) return ArrayList()

            val json = sharedPreferences.getString(key, null) ?: return ArrayList()
            val type: Type = object : TypeToken<ArrayList<ChatMessageData>>() {}.type
            var cachedMessages = Gson().fromJson<Any>(json, type) as ArrayList<ChatMessageData>
            if (cachedMessages == null) { cachedMessages = ArrayList() }

            return cachedMessages
        }

    }

}