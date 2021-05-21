package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.user.FirebaseUserBundleProvider
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.database.user.UserBundle
import com.github.gogetters.letsgo.database.user.UserBundleProvider

class ProfileEditActivity : AppCompatActivity() {

    private val tag = "Profile"

    private lateinit var userBundleProvider: UserBundleProvider
    private lateinit var userBundle: UserBundle
    private lateinit var user: LetsGoUser

    private lateinit var nickEditText : EditText
    private lateinit var firstEditText : EditText
    private lateinit var lastEditText : EditText
    private lateinit var countryEditText : EditText
    private lateinit var cityEditText : EditText

    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        userBundleProvider = intent.getSerializableExtra("UserBundleProvider") as UserBundleProvider
        userBundle = userBundleProvider.getUserBundle()!!
        user = userBundle.getUser()

        saveButton = findViewById(R.id.profile_edit_button_save)
        saveButton.setOnClickListener {
            user.nick = nickEditText.text.toString()
            user.first = firstEditText.text.toString()
            user.last = lastEditText.text.toString()
            user.country = countryEditText.text.toString()
            user.city = cityEditText.text.toString()

            user.uploadUserData().continueWith { returnToProfile() }
        }

        nickEditText = findViewById(R.id.profile_edit_nick)
        firstEditText = findViewById(R.id.profile_edit_first)
        lastEditText = findViewById(R.id.profile_edit_last)
        countryEditText = findViewById(R.id.profile_edit_country)
        cityEditText = findViewById(R.id.profile_edit_city)

        nickEditText.setText(user.nick)
        firstEditText.setText(user.first)
        lastEditText.setText(user.last)
        countryEditText.setText(user.country)
        cityEditText.setText(user.city)
    }

    private fun returnToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("UserBundleProvider", userBundleProvider)
        startActivity(intent)
    }

    // TODO
    // - Add logic for saving user info! (not the profile pic)
}