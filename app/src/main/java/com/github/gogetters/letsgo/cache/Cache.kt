package com.github.gogetters.letsgo.cache

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.google.firebase.auth.FirebaseAuth

class Cache {

    companion object {

        private const val PREF_ID = "sharedPrefs"

        /**
         * Enables saving user info in local cache for the profile activity UI
         */
        fun saveUserProfile(act: AppCompatActivity, user: LetsGoUser) {

            val sharedPreferences: SharedPreferences = act.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.apply {

                putString("userNick", user.nick)
                putString("userFirst", user.first)
                putString("userLast", user.last)
                putString("userCountry", user.country)
                putString("userCity", user.city)

            }.apply()

        }

        /**
         * Enables loading user info from local cache for the profile activity UI
         */
        fun loadUserProfile(act: AppCompatActivity): LetsGoUser? {

            val sharedPreferences: SharedPreferences = act.applicationContext.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE)

            val user = LetsGoUser("0")
            user.nick = sharedPreferences.getString("userNick", "")
            user.first = sharedPreferences.getString("userFirst", "")
            user.last = sharedPreferences.getString("userLast", "")
            user.country = sharedPreferences.getString("userCountry", "")
            user.city = sharedPreferences.getString("userCity", "")

            return user

        }

    }

}