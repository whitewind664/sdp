package com.github.gogetters.letsgo.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.collections.ArrayList

object Cache {

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

    private const val LAST_MESSAGE_LIST_ID = "lastMessageListID"

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


//        if (loggedInUid != null) {
//            val cachedUid = sharedPreferences.getString("userUid", null)
//            if (cachedUid == null || loggedInUid != cachedUid) {
//                return null
//            }
//        }

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
    fun saveChatData(sharedPreferences: SharedPreferences?, chatPartnerUid: String?, items: ArrayList<ChatMessageData>?) {

        if (sharedPreferences == null || chatPartnerUid == null || items == null) return

        val editor = sharedPreferences.edit()
        val json = Gson().toJson(items)
        editor.apply {

            putString(chatPartnerUid, json)

        }.apply()
    }

    /**
     * Enables loading chat messages in local cache for the ChatActivity
     */
    fun loadChatData(sharedPreferences: SharedPreferences?, chatPartnerUid: String?): ArrayList<ChatMessageData> {

        if (sharedPreferences == null) return ArrayList()

        val json = sharedPreferences.getString(chatPartnerUid, null) ?: return ArrayList()
        val type: Type = object : TypeToken<ArrayList<ChatMessageData>>() {}.type
        var cachedMessages = Gson().fromJson<Any>(json, type) as ArrayList<ChatMessageData>?
        if (cachedMessages == null) { cachedMessages = ArrayList() }

        return cachedMessages
    }

    fun readStoredUid(sharedPreferences: SharedPreferences?):String? {
        if (sharedPreferences != null) {
            return sharedPreferences.getString("userUid", null)
        }
        return null
    }


    /**
     * Enables saving last chat messages list in local cache for the ChatLastMessagesActivity
     */
    fun saveLastChatData(act: AppCompatActivity, items: MutableCollection<ChatMessageData>) {

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
    fun loadLastChatData(act: AppCompatActivity): MutableCollection<ChatMessageData> {

        val sharedPreferences = act.applicationContext.getSharedPreferences(
            PREF_ID,
            Context.MODE_PRIVATE
        )

        val json = sharedPreferences.getString(LAST_MESSAGE_LIST_ID, null)
        if (json == null) return mutableListOf()
        val type: Type = object : TypeToken<MutableCollection<ChatMessageData>?>() {}.type
        var cachedMessages = Gson().fromJson<Any>(json, type) as MutableCollection<ChatMessageData>?
        if (cachedMessages == null) { cachedMessages = mutableListOf() }

        return cachedMessages

    }

}