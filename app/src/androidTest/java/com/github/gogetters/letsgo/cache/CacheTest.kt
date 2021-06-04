package com.github.gogetters.letsgo.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.github.gogetters.letsgo.cache.Cache.Companion.loadChatData
import com.github.gogetters.letsgo.cache.Cache.Companion.loadUserProfile
import com.github.gogetters.letsgo.cache.Cache.Companion.saveChatData
import com.github.gogetters.letsgo.cache.Cache.Companion.saveUserProfile
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.database.user.LetsGoUser
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Tests that modify the shared preferences.
 */
@SmallTest
class SharedPreferencesTest {

    private var sharedPreferences: SharedPreferences? = null
    private var context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    companion object {
        private const val PREFS_NAME    = "prefs"
        private const val CHAT_MSG_ID   = "chatId"
        // user info
        private const val UID           = "userUid"
        private const val NICK          = "testerDude"
        private const val FIRST         = "John"
        private const val LAST          = "Donut"
        private const val COUNTRY       = "USA"
        private const val CITY          = "Springfield"
        // chat info
        private const val CHAT_ID       = "id"
        private const val CHAT_TEXT     = "text"
        private const val CHAT_FROMID   = "fromId"
        private const val CHAT_TOID     = "toId"
        private const val CHAT_SENDTIME = -1L

    }

    @Before
    fun before() {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Test
    @Throws(Exception::class)
    fun success_saveUserProfile() {

        val user = LetsGoUser(UID)
        user.nick = NICK
        user.first = FIRST
        user.last = LAST
        user.country = COUNTRY
        user.city = CITY

        saveUserProfile(sharedPreferences, user)

        assertEquals(UID, sharedPreferences!!.getString(Cache.USER_UID, ""))
        assertEquals(NICK, sharedPreferences!!.getString(Cache.USER_NICK, ""))
        assertEquals(FIRST, sharedPreferences!!.getString(Cache.USER_FIRST, ""))
        assertEquals(LAST, sharedPreferences!!.getString(Cache.USER_LAST, ""))
        assertEquals(COUNTRY, sharedPreferences!!.getString(Cache.USER_COUNTRY, ""))
        assertEquals(CITY, sharedPreferences!!.getString(Cache.USER_CITY, ""))
    }

    @Test
    @Throws(Exception::class)
    fun success_loadUserProfile() {

        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.apply {

            putString(Cache.USER_UID, UID)
            putString(Cache.USER_NICK, NICK)
            putString(Cache.USER_FIRST, FIRST)
            putString(Cache.USER_LAST, LAST)
            putString(Cache.USER_COUNTRY, COUNTRY)
            putString(Cache.USER_CITY, CITY)

        }.apply()

        val user = loadUserProfile(sharedPreferences)

        assertEquals(UID, user?.uid)
        assertEquals(NICK, user?.nick)
        assertEquals(FIRST, user?.first)
        assertEquals(LAST, user?.last)
        assertEquals(COUNTRY, user?.country)
        assertEquals(CITY, user?.city)
    }

    @Test
    @Throws(Exception::class)
    fun success_saveAndLoadChatData() {

        val chat = ChatMessageData(
            CHAT_ID,
            CHAT_TEXT,
            CHAT_FROMID,
            CHAT_TOID,
            CHAT_SENDTIME)

        saveChatData(sharedPreferences, CHAT_MSG_ID, arrayListOf(chat))
        val cachedChat = loadChatData(sharedPreferences, CHAT_MSG_ID)

        assertEquals(cachedChat.get(0).id, CHAT_ID)
        assertEquals(cachedChat.get(0).text, CHAT_TEXT)
        assertEquals(cachedChat.get(0).fromId, CHAT_FROMID)
        assertEquals(cachedChat.get(0).toId, CHAT_TOID)
        assertEquals(cachedChat.get(0).sendTime, CHAT_SENDTIME)

    }

    @After
    fun after() {
        sharedPreferences!!.edit().putString(Cache.USER_UID, null).apply()
        sharedPreferences!!.edit().putString(Cache.USER_NICK, null).apply()
        sharedPreferences!!.edit().putString(Cache.USER_FIRST, null).apply()
        sharedPreferences!!.edit().putString(Cache.USER_LAST, null).apply()
        sharedPreferences!!.edit().putString(Cache.USER_COUNTRY, null).apply()
        sharedPreferences!!.edit().putString(Cache.USER_CITY, null).apply()
    }

}