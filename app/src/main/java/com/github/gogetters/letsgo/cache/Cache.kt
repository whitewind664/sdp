package com.github.gogetters.letsgo.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.chat.views.ChatNewMessageItem
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import java.lang.reflect.Type


class Cache {

    companion object {

        private const val PREF_ID = "sharedPrefs"
        private const val USER_LIST_ID = "userList"
        private const val LAST_MESSAGE_LIST_ID = "lastMessageList"
        private const val CHAT_MESSAGE_LIST_ID = "chatMessageList"

        // User attributes
        private const val USER_NICK = "userNick"
        private const val USER_FIRST = "userFirst"
        private const val USER_LAST = "userLast"
        private const val USER_COUNTRY = "userCountry"
        private const val USER_CITY = "userCity"

        /**
         * Enables saving user info in local cache for the profile activity UI
         */
        fun saveUserProfile(act: AppCompatActivity, user: LetsGoUser) {

            val sharedPreferences: SharedPreferences = act.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.apply {

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
        fun loadUserProfile(act: AppCompatActivity): LetsGoUser? {

            val sharedPreferences: SharedPreferences = act.applicationContext.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )

            val user = LetsGoUser("0")
            user.nick = sharedPreferences.getString(USER_NICK, "")
            user.first = sharedPreferences.getString(USER_FIRST, "")
            user.last = sharedPreferences.getString(USER_LAST, "")
            user.country = sharedPreferences.getString(USER_COUNTRY, "")
            user.city = sharedPreferences.getString(USER_CITY, "")

            return user
        }

        /**
         * Enables saving users list in local cache for the ChatNewMessageActivity
         */
        fun saveUserData(act: AppCompatActivity, items: ArrayList<ChatNewMessageItem>) {

            val sharedPreferences = act.applicationContext.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )

            val editor = sharedPreferences.edit()
            val json = Gson().toJson(items)
            editor.apply {

                putString(USER_LIST_ID, json)

            }.apply()
        }

        /**
         * Enables loading users list in local cache for the ChatNewMessageActivity
         */
        fun loadUserData(act: AppCompatActivity): ArrayList<ChatNewMessageItem> {

            val sharedPreferences = act.applicationContext.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )

            val json = sharedPreferences.getString(USER_LIST_ID, null)
            val type: Type = object : TypeToken<ArrayList<ChatNewMessageItem?>?>() {}.type
            var cachedUsers = Gson().fromJson<Any>(json, type) as ArrayList<ChatNewMessageItem>
            if (cachedUsers == null) { cachedUsers = arrayListOf() }

            return cachedUsers
        }

        /**
         * Enables saving chat messages list in local cache for the ChatActivity
         */
        fun saveChatData(act: AppCompatActivity, items: ArrayList<Item<ViewHolder>>) {

            val sharedPreferences = act.applicationContext.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )

            val editor = sharedPreferences.edit()
            val json = Gson().toJson(items)
            editor.apply {

                putString(CHAT_MESSAGE_LIST_ID, json)

            }.apply()
        }

        /**
         * Enables loading chat messages in local cache for the ChatActivity
         */
        fun loadChatData(act: AppCompatActivity): ArrayList<Item<ViewHolder>> {

            val sharedPreferences = act.applicationContext.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )

            val json = sharedPreferences.getString(CHAT_MESSAGE_LIST_ID, null)
            val type: Type = object : TypeToken<ArrayList<Item<ViewHolder>?>?>() {}.type
            var cachedMessages = Gson().fromJson<Any>(json, type) as ArrayList<Item<ViewHolder>>
            if (cachedMessages == null) { cachedMessages = arrayListOf() }

            return cachedMessages
        }

        /**
         * Enables saving last chat messages list in local cache for the ChatLastMessagesActivity
         */
        fun saveLastChatData(act: AppCompatActivity, items: HashMap<String, ChatMessageData>) {

            val sharedPreferences = act.applicationContext.getSharedPreferences(
                PREF_ID,
                Context.MODE_PRIVATE
            )

            val editor = sharedPreferences.edit()
            val json = Gson().toJson(items)
            editor.apply {

                putString(LAST_MESSAGE_LIST_ID, json)

            }.apply()
        }

        /**
         * Enables loading last chat messages in local cache for the ChatLastMessagesActivity
         */
        fun loadLastChatData(act: AppCompatActivity): HashMap<String, ChatMessageData> {
            // TODO: implement
            return HashMap<String, ChatMessageData>()
        }

    }

}