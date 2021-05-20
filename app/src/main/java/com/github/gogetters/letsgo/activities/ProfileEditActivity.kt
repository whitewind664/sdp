package com.github.gogetters.letsgo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.gogetters.letsgo.R

class ProfileEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
    }

    // TODO
    // - Add logic for saving user info! (not the profile pic)
}